package com.joel.communication_android.states

import com.joel.communication_core.response.ErrorResponse


sealed class AsyncState<out T> {
    data class Success<T>(val data: T) : AsyncState<T>()
    data class Error(val error: ErrorResponse) : AsyncState<Nothing>()
    object Empty : AsyncState<Nothing>()
}
