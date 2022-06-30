package com.android.sample.app.base

import android.content.Context
import com.android.sample.app.R
import com.android.sample.app.util.ViewState
import com.android.sample.app.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

abstract class BaseRepository<T>(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) {
    protected abstract suspend fun query(id: String?): T?

    protected abstract suspend fun fetch(url: String?): T

    protected abstract suspend fun saveFetchResult(t: T)

    fun getResult(id: String?, url: String?): Flow<ViewState<T?>> = flow {
        emit(ViewState.Loading)
        query(id)?.let {
            // ****** STEP 1: VIEW CACHE ******
            emit(ViewState.Success(it))
            try {
                // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh(url)
                // ****** STEP 3: VIEW CACHE ******
                emit(ViewState.Success(query(id)))
            } catch (t: Throwable) {
                Timber.e(t)
            }
        } ?: run {
            if (context.isNetworkAvailable()) {
                try {
                    // ****** STEP 1: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                    refresh(url)
                    // ****** STEP 2: VIEW CACHE ******
                    emit(ViewState.Success(query(id)))
                } catch (t: Throwable) {
                    emit(ViewState.Error(context.getString(R.string.failed_loading_msg)))
                }
            } else {
                emit(ViewState.Error(context.getString(R.string.failed_network_msg)))
            }
        }
    }.flowOn(ioDispatcher)

    private suspend fun refresh(url: String?) {
        saveFetchResult(fetch(url))
    }
}