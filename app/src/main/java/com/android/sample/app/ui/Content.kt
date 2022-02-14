package com.android.sample.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.android.sample.app.base.BaseViewModel
import com.android.sample.app.ui.common.ErrorScreen
import com.android.sample.app.ui.common.ProgressScreen
import com.android.sample.app.util.ViewState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun <T> Content(
    viewModel: BaseViewModel<T>,
    SuccessScreen: @Composable() (t: T) -> Unit
) {
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