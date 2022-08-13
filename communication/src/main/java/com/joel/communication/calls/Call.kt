package com.joel.communication.calls

import com.joel.communication.extensions.toJson
import com.joel.communication.extensions.urlWithPath
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.request.RequestBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http.HttpMethod

class Call internal constructor() {

    internal fun request(builder: RequestBuilder. () -> Unit = {}): CommunicationRequest {
        val callBuilder = RequestBuilder().also(builder)

        val requestBuilder = Request.Builder()
            .url(urlWithPath(
                callBuilder.path,
                callBuilder.method,
                callBuilder.parameters
            ))

        callBuilder.headers.forEach {
            requestBuilder.addHeader(it.first.header, it.second)
        }

        if (HttpMethod.requiresRequestBody(callBuilder.method.name) && callBuilder.body == null) {
            if (callBuilder.parameters.isEmpty())
                callBuilder.body = "".toRequestBody()
            else
                callBuilder.body = callBuilder.parameters.toJson(callBuilder.dateFormat).toRequestBody("application/json; charset=utf-8".toMediaType())
        }

        requestBuilder.method(callBuilder.method.name, callBuilder.body)

        return CommunicationRequest(requestBuilder, callBuilder)
    }
}