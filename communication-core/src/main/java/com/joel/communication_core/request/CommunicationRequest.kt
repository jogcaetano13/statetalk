package com.joel.communication_core.request

import com.joel.communication_core.alias.Headers
import com.joel.communication_core.enums.HttpHeader
import com.joel.communication_core.enums.HttpMethod
import com.joel.communication_core.extensions.apiError
import com.joel.communication_core.extensions.toHttpMethod
import com.joel.communication_core.response.CommunicationResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI

class CommunicationRequest internal constructor(
    @PublishedApi
    internal val requestBuilder: Request.Builder,
    val builder: ImmutableRequestBuilder,
    @PublishedApi
    internal val client: OkHttpClient
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
            val first = HttpHeader.find(requestHeader.first)!!
            Pair(first, requestHeader.second)
        }.toMutableList()

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
