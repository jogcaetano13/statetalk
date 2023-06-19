package com.joel.communication_core.client

import com.joel.communication_core.request.CommunicationRequest
import com.joel.communication_core.request.RequestBuilder
import okhttp3.OkHttpClient

internal class ClientImpl internal constructor(
    private val client: OkHttpClient,
    private val baseUrl: String
): Client {

    override fun call(builder: RequestBuilder.() -> Unit): CommunicationRequest {
        return request(builder)
    }

    private fun request(builder: RequestBuilder. () -> Unit = {}): CommunicationRequest {
        val callBuilder = RequestBuilder(client, baseUrl).also(builder)
        val request = callBuilder.build()

        return CommunicationRequest(request, callBuilder, client, baseUrl)
    }
}