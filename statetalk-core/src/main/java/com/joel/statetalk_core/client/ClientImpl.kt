package com.joel.statetalk_core.client

import com.joel.statetalk_core.request.RequestBuilder
import com.joel.statetalk_core.request.StateTalkRequest
import okhttp3.OkHttpClient

internal class ClientImpl internal constructor(
    private val client: OkHttpClient,
    private val baseUrl: String
): Client {

    override fun call(builder: RequestBuilder.() -> Unit): StateTalkRequest {
        return request(builder)
    }

    private fun request(builder: RequestBuilder. () -> Unit = {}): StateTalkRequest {
        val callBuilder = RequestBuilder(client, baseUrl).also(builder)
        val requestBuilder = callBuilder.build()

        return StateTalkRequest(requestBuilder, callBuilder, client, baseUrl)
    }
}