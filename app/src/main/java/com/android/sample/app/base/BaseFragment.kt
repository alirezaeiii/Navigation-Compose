package com.android.sample.app.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import com.android.sample.app.ui.common.ErrorScreen
import com.android.sample.app.ui.common.ProgressScreen
import com.android.sample.app.util.ViewState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

abstract class BaseFragment<T> : Fragment() {

    protected abstract val viewModel: BaseViewModel<T>

    @Composable
    protected abstract fun SuccessScreen(t: T)

    @Composable
    protected fun Content() {
        when (val viewState = viewModel.stateFlow.collectAsState().value) {
            is ViewState.Loading -> ProgressScreen()
            is ViewState.Success -> {
                val isRefreshing by viewModel.isRefreshing.collectAsState()
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = { viewModel.refresh() }
                ) {
                    viewState.data?.let { SuccessScreen(t = it) }
                }
            }
            is ViewState.Error ->
                ErrorScreen(message = viewState.message, refresh = viewModel::refresh)
        }
    }
}