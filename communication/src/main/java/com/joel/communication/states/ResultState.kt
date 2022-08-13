package com.joel.communication.states

import com.joel.communication.models.ErrorResponse

sealed class ResultState<out T : Any> {
    data class Error(val error: ErrorResponse) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
    object Empty : ResultState<Nothing>()
    data class Success<out T : Any>(val data: T) : ResultState<T>()
}
