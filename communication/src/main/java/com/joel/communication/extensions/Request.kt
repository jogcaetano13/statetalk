package com.joel.communication.extensions

import com.joel.communication.alias.Parameters
import com.joel.communication.client.Client
import com.joel.communication.enums.HttpMethod
import com.joel.communication.request.RequestBuilder
import okhttp3.Request
import java.net.*

@PublishedApi
internal fun Request.Builder.updateUrl(builder: RequestBuilder) {
    url(urlWithPath(
        builder.path,
        builder.method,
        builder.parameters
    ))
}

internal fun encodeParameterInUrl(method: HttpMethod): Boolean = when(method) {
    HttpMethod.GET, HttpMethod.DELETE, HttpMethod.HEAD -> true
    else -> false
}

internal fun queryFromParameters(parameters: Parameters?): String = parameters.orEmpty()
    .filterNot { it.second == null }
    .map { (key, value) -> URLEncoder.encode(key,"UTF-8") to URLEncoder.encode("$value", "UTF-8") }
    .joinToString("&") { (key, value) -> "$key=$value" }

internal fun createUrl(path: String): URL {
    val url = try {
        URL(path)
    } catch (e: MalformedURLException) {
        URL(Client.instance.baseUrl + if (path.startsWith('/') or path.isEmpty()) path else "/$path")
    }

    val uri = try {
        url.toURI()
    } catch (e: URISyntaxException) {
        URI(url.protocol, url.userInfo, url.host, url.port, url.path, url.query, url.ref)
    }

    return URL(uri.toASCIIString())
}

internal fun urlWithPath(path: String, method: HttpMethod, parameters: Parameters?): URL {
    var modifiedPath = path

    if (encodeParameterInUrl(method)) {
        var querySign = ""
        val queryParameterString = queryFromParameters(parameters)

        if (queryParameterString.isNotEmpty()) {
            if (path.count() > 0)
                querySign = if (path.last() == '?') "" else "?"
        }

        modifiedPath += (querySign + queryParameterString)
    }

    return createUrl(modifiedPath)
}