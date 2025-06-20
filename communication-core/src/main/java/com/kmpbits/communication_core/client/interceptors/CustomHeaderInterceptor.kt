package com.kmpbits.communication_core.client.interceptors

import com.kmpbits.communication_core.alias.Headers
import okhttp3.Interceptor
import okhttp3.Response

internal class CustomHeaderInterceptor(
    private val headers: Headers
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticateRequest = request.newBuilder()

        headers.forEach {
            authenticateRequest.addHeader(it.first.header, it.second)
        }

        return chain.proceed(authenticateRequest.build())
    }

    internal fun getHeaders() = headers.toList()
}