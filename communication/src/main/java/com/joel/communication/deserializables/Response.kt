package com.joel.communication.deserializables

import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.dispatchers.CommunicationDispatcherImpl
import com.joel.communication.extensions.apiCall
import com.joel.communication.extensions.toList
import com.joel.communication.extensions.toModel
import com.joel.communication.extensions.toModelWrapped
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.response.CommunicationResponse
import com.joel.communication.states.AsyncState

suspend fun CommunicationRequest.response(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl
): CommunicationResponse {
    return when(val call = apiCall(dispatcher)) {
        AsyncState.Empty -> throw IllegalStateException("Empty is not used!")
        is AsyncState.Error -> CommunicationResponse(call.error.code, headers, call.error.errorBody)
        is AsyncState.Success -> CommunicationResponse(call.data.code, headers, call.data.body)
    }
}

inline fun <reified T> CommunicationResponse.toModel(
    datePattern: String = "yyyy-MM-dd'T'HH:mm:ss.ZZZZZZZ"
): T? = body?.toModel(datePattern)

inline fun <reified T> CommunicationResponse.toList(
    datePattern: String = "yyyy-MM-dd'T'HH:mm:ss.ZZZZZZZ"
): List<T> = body?.toList(datePattern) ?: emptyList()

inline fun <reified T> CommunicationResponse.toModelWrapped(
    datePattern: String = "yyyy-MM-dd'T'HH:mm:ss.ZZZZZZZ"
): T? = body?.toModelWrapped(datePattern)