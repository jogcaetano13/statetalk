package com.joel.communication_core.client

import com.joel.communication_core.extensions.toJson
import com.joel.communication_core.extensions.toName
import com.joel.communication_core.extensions.urlWithPath
import com.joel.communication_core.request.CommunicationRequest
import com.joel.communication_core.request.RequestBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http.HttpMethod

internal class ClientImpl internal constructor(
    private val client: OkHttpClient,
    private val baseUrl: String
): Client {

    override fun call(builder: RequestBuilder.() -> Unit): CommunicationRequest {
        return request(builder)
    }

    private fun request(builder: RequestBuilder. () -> Unit = {}): CommunicationRequest {
        val callBuilder = RequestBuilder().also(builder)

        val requestBuilder = Request.Builder()
            .url(urlWithPath(
                baseUrl,
                callBuilder.path,
                callBuilder.method,
                callBuilder.parameters
            ))

        callBuilder.headers.forEach {
            requestBuilder.addHeader(it.first.header, it.second)
        }

        val method = callBuilder.method.toName()

        if (HttpMethod.requiresRequestBody(method) && callBuilder.body == null) {
            if (callBuilder.parameters.isEmpty())
                callBuilder.body = "".toRequestBody()
            else
                callBuilder.body = callBuilder.parameters.toJson(callBuilder.dateFormat).toRequestBody("application/json; charset=utf-8".toMediaType())
        }

        requestBuilder.method(method, callBuilder.body)

        return CommunicationRequest(requestBuilder, callBuilder, client)
    }
}