package com.joel.statetalk_core.deserializables

import com.joel.statetalk_core.exceptions.HttpException
import com.joel.statetalk_core.extensions.toModel
import com.joel.statetalk_core.request.CommunicationRequest

suspend inline fun <reified T> CommunicationRequest.responseToModel(dateFormat: String = builder.dateFormat): T {
    val apiCall = response()

    if (apiCall.isSuccess) {
        return apiCall.toModel(dateFormat)
    }

    throw HttpException(apiCall.code, apiCall.errorBody)
}