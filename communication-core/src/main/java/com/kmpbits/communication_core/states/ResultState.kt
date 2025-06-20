package com.kmpbits.communication_core.states

import com.kmpbits.communication_core.response.ErrorResponse

sealed class ResultState<out T> {
    data class Error<T>(val error: ErrorResponse, val data: T? = null) : ResultState<T>()
    data class Loading<T>(val data: T? = null) : ResultState<T>()
    object Empty : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
}
