package com.joel.statetalk_core.request

import com.joel.statetalk_core.alias.Header
import com.joel.statetalk_core.enums.HttpHeader
import com.joel.statetalk_core.enums.HttpMethod
import com.joel.statetalk_core.extensions.apiError
import com.joel.statetalk_core.extensions.toHttpMethod
import com.joel.statetalk_core.extensions.updateUrl
import com.joel.statetalk_core.response.StateTalkResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.annotations.VisibleForTesting
import java.net.URI

class StateTalkRequest internal constructor(
    @PublishedApi
    internal val requestBuilder: Request.Builder,
    @PublishedApi
    internal val builder: RequestBuilder,
    @PublishedApi
    internal val client: OkHttpClient,
    @PublishedApi
    internal val baseUrl: String,
) {
    val immutableRequestBuilder = ImmutableRequestBuilder(
        preCall = builder.preCall,
        headers = builder.headers,
        dateFormat = builder.dateFormat,
        builder = builder
    )

    fun updateUrl() {
        requestBuilder.updateUrl(baseUrl, builder)
    }

    internal val request
        get() = requestBuilder.build()

    val url: URI
        get() = request.url.toUri()

    val isHttps: Boolean
        get() = request.isHttps

    val method: HttpMethod
        get() = request .method.toHttpMethod()

    val headers: List<Pair<HttpHeader, String>>
        get() = request.headers.map {
            Header(HttpHeader.custom(it.first), it.second)
        }

    companion object {

        @VisibleForTesting
        fun testRequest() = StateTalkRequest(
            Request.Builder(),
            RequestBuilder(OkHttpClient(), "baseUrl"),
            OkHttpClient(),
            "baseUrl"
        )
    }

    suspend fun response(): StateTalkResponse {
        return try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }

            if (response.isSuccessful) {
                StateTalkResponse(
                    code = response.code,
                    headers = headers,
                    body = response.body,
                    errorBody = null
                )
            } else {
                StateTalkResponse(
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

            StateTalkResponse(
                code = 500,
                headers = headers,
                body = null,
                errorBody = e.apiError.errorBody
            )
        }
    }
}
