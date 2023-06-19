package com.joel.communication_core.deserializables

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joel.communication_core.exceptions.HttpException
import com.joel.communication_core.request.CommunicationRequest
import com.joel.communication_core.response.CommunicationResponse

suspend inline fun <reified T> CommunicationRequest.responseToModel(): T {
    val apiCall = response()

    if (apiCall.isSuccess) {
        return apiCall.toModel()
    }

    throw HttpException(apiCall.code, apiCall.errorBody)
}

@PublishedApi
internal inline fun <reified T> CommunicationResponse.toModel(): T {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson(body?.string(), type)
}