package com.kmpbits.communication_core.extensions

import com.kmpbits.communication_core.alias.Parameters
import com.kmpbits.communication_core.enums.HttpMethod
import com.kmpbits.communication_core.request.RequestBuilder
import okhttp3.Request
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.net.URLEncoder

@PublishedApi
internal fun Request.Builder.updateUrl(
    baseUrl: String,
    builder: RequestBuilder
) {
    val url = urlWithPath(
        baseUrl,
        builder.path,
        builder.method,
        builder.parameters
    )

    url(url)
}

internal fun encodeParameterInUrl(method: HttpMethod): Boolean = when(method) {
    HttpMethod.Get, HttpMethod.Delete, HttpMethod.Head -> true
    else -> false
}

internal fun queryFromParameters(parameters: Parameters?): String = parameters.orEmpty()
    .filterNot { it.second == null }
    .map { (key, value) -> URLEncoder.encode(key,"UTF-8") to URLEncoder.encode("$value", "UTF-8") }
    .joinToString("&") { (key, value) -> "$key=$value" }

internal fun createUrl(
    baseUrl: String,
    path: String
): URL {
    val url = try {
        URL(path)
    } catch (e: MalformedURLException) {
        URL(baseUrl + if (path.startsWith('/') or path.isEmpty()) path else "/$path")
    }

    val uri = try {
        url.toURI()
    } catch (e: URISyntaxException) {
        URI(url.protocol, url.userInfo, url.host, url.port, url.path, url.query, url.ref)
    }

    return URL(uri.toASCIIString())
}

internal fun urlWithPath(
    baseUrl: String,
    path: String,
    method: HttpMethod,
    parameters: Parameters?
): URL {
    var modifiedPath = path

    if (encodeParameterInUrl(method)) {
        var querySign = ""
        val queryParameterString = queryFromParameters(parameters)

        if (queryParameterString.isNotEmpty()) {
            if (path.isNotEmpty())
                querySign = if (path.last() == '?') "" else "?"
        }

        modifiedPath += (querySign + queryParameterString)
    }

    return createUrl(baseUrl, modifiedPath)
}