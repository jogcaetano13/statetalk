package com.joel.communication_core.deserializables

import com.joel.communication_core.exceptions.HttpException
import com.joel.communication_core.extensions.toModel
import com.joel.communication_core.request.CommunicationRequest

suspend inline fun <reified T> CommunicationRequest.responseToModel(dateFormat: String = builder.dateFormat): T {
    val apiCall = response()

    if (apiCall.isSuccess) {
        return apiCall.toModel(dateFormat)
    }

    throw HttpException(apiCall.code, apiCall.errorBody)
}