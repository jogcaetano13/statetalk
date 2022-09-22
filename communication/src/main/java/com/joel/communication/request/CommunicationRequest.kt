package com.joel.communication.request

import androidx.annotation.VisibleForTesting
import com.joel.communication.alias.Headers
import com.joel.communication.enums.HttpHeader
import com.joel.communication.enums.HttpMethod
import com.joel.communication.extensions.toHttpMethod
import okhttp3.Request
import java.net.URI

/**
 * @author joelcaetano
 * Created 28/11/2021 at 15:14
 */
data class CommunicationRequest internal constructor(
    @PublishedApi
    internal val requestBuilder: Request.Builder,
    @PublishedApi
    internal val builder: RequestBuilder
) {

    internal val request
        get() = requestBuilder.build()

    val url: URI
        get() = request.url.toUri()

    val isHttps: Boolean
        get() = request.isHttps

    val method: HttpMethod
        get() = request.method.toHttpMethod()

    val headers: Headers
        get() = request.headers.map { requestHeader ->
            val first = HttpHeader.find(requestHeader.first) ?: HttpHeader(requestHeader.first)
            Pair(first, requestHeader.second)
        }.toMutableList()

    companion object {

        @VisibleForTesting
        fun testRequest() = CommunicationRequest(
            Request.Builder(),
            RequestBuilder()
        )
    }
}