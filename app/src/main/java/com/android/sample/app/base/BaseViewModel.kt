package com.android.sample.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sample.app.util.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class BaseViewModel<T>(
    private val repository: BaseRepository<T>,
    private val linkId: String? = null,
    private val linkUrl: String? = null
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ViewState<T?>>(ViewState.Loading)
    val stateFlow: StateFlow<ViewState<T?>>
        get() = _stateFlow

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            repository.getResult(linkId, linkUrl).collect {
                _stateFlow.tryEmit(it)
            }
            _isRefreshing.emit(false)
        }
    }
}