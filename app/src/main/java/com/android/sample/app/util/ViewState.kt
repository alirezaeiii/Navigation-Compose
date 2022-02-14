package com.android.sample.app.util

sealed class ViewState<out R> {
    object Loading : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error(val message: String) : ViewState<Nothing>()
}