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
        is AsyncState.Error -> CommunicationResponse(
            code = call.error.code, headers,
            body = null,
            errorBody = call.error.errorBody
        )
        is AsyncState.Success -> CommunicationResponse(
            code = call.data.code, headers,
            body = call.data.body,
            errorBody = null
        )
    }
}

inline fun <reified T> CommunicationResponse.toModel(
    datePattern: String = "yyyy-MM-dd'T'HH:mm:ss.ZZZZZZZ"
): T? = body?.string()?.toModel(datePattern)

inline fun <reified T> CommunicationResponse.toList(
    datePattern: String = "yyyy-MM-dd'T'HH:mm:ss.ZZZZZZZ"
): List<T> = body?.string()?.toList(datePattern) ?: emptyList()

inline fun <reified T> CommunicationResponse.toModelWrapped(
    datePattern: String = "yyyy-MM-dd'T'HH:mm:ss.ZZZZZZZ"
): T? = body?.string()?.toModelWrapped(datePattern)