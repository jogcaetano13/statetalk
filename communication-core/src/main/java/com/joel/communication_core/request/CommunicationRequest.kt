package com.joel.communication_core.request

import com.joel.communication_core.alias.Header
import com.joel.communication_core.enums.HttpHeader
import com.joel.communication_core.enums.HttpMethod
import com.joel.communication_core.extensions.apiError
import com.joel.communication_core.extensions.toHttpMethod
import com.joel.communication_core.extensions.urlWithPath
import com.joel.communication_core.response.CommunicationResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI

class CommunicationRequest internal constructor(
    @PublishedApi
    internal val okhttpRequest: Request,
    @PublishedApi
    internal val builder: RequestBuilder,
    @PublishedApi
    internal val client: OkHttpClient,
    @PublishedApi
    internal val baseUrl: String,
    private val requestHeaders: okhttp3.Headers
) {
    val immutableRequestBuilder = ImmutableRequestBuilder(
        preCall = builder.preCall,
        headers = builder.headers,
        dateFormat = builder.dateFormat,
        builder = builder
    )

    fun updateUrl() {
        urlWithPath(
            baseUrl,
            builder.path,
            builder.method,
            builder.parameters
        )
    }

    internal val request
        get() = okhttpRequest

    val url: URI
        get() = okhttpRequest.url.toUri()

    val isHttps: Boolean
        get() = okhttpRequest.isHttps

    val method: HttpMethod
        get() = okhttpRequest.method.toHttpMethod()

    val headers: List<Pair<HttpHeader, String>>
        get() = requestHeaders.map {
            Header(HttpHeader.custom(it.first), it.second)
        }

    suspend fun response(): CommunicationResponse {
        return try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }

            if (response.isSuccessful) {
                CommunicationResponse(
                    code = response.code,
                    headers = headers,
                    body = response.body,
                    errorBody = null
                )
            } else {
                CommunicationResponse(
                    code = response.code,
                    headers = headers,
                    body = null,
                    errorBody = response.body?.string()
                )
            }

        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }

            CommunicationResponse(
                code = 500,
                headers = headers,
                body = null,
                errorBody = e.apiError.errorBody
            )
        }
    }
}
