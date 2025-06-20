package com.kmpbits.communication_core.client

import com.kmpbits.communication_core.request.RequestBuilder
import com.kmpbits.communication_core.request.CommunicationRequest
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
        val requestBuilder = callBuilder.build()

        return CommunicationRequest(requestBuilder, callBuilder, client, baseUrl)
    }
}