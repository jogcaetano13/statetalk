package com.joel.communication_core.deserializables

import com.joel.communication_core.extensions.apiError
import com.joel.communication_core.request.CommunicationRequest
import com.joel.communication_core.states.ResultState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

inline fun <reified T : Any> CommunicationRequest.responseFlow() = flow {
    emit(ResultState.Loading())

    val callResponse = response()

    if (callResponse.isSuccess) {
        val model = callResponse.toModel<T>()
        emit(ResultState.Success(model))
    }
}.catch {
    emit(ResultState.Error(it.apiError))
}