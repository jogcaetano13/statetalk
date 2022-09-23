package com.joel.communication.extensions

import com.joel.communication.enums.ErrorResponseType
import com.joel.communication.models.ErrorResponse
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val Throwable.apiError
    get() = when(this) {
        is SocketTimeoutException -> ErrorResponse(408, "Timeout", ErrorResponseType.TIMEOUT)
        is UnknownHostException -> ErrorResponse(500, "No Internet", ErrorResponseType.INTERNET_CONNECTION)
        else -> ErrorResponse(500, message, ErrorResponseType.UNKNOWN)
    }

fun Response.getApiError(): ErrorResponse {
    val errorString = body?.string()

    return if (errorString != null) {
        if (code in 500..599)
            return ErrorResponse(code, errorString, type = ErrorResponseType.SERVICE_UNAVAILABLE)

        ErrorResponse(code, errorString, ErrorResponseType.HTTP)

    } else {
        getGenericError()
    }
}

/**
 * Check if the string is a Json object
 * @return If is or not a Json object
 */
internal fun String.toJsonObject(): JSONObject? {
    if (isEmpty())
        return null

    return try {
        JSONObject(this)

    } catch (e: JSONException) {
        e.printStackTrace()
        null
    }
}

private fun getGenericError() = ErrorResponse(500,"General error", ErrorResponseType.UNKNOWN)

private fun JSONObject.getStringOrNull(name: String): String? = if (has(name)) getString(name) else null

private fun JSONObject.getIntOrNull(name: String): Int? = if (has(name)) getInt(name) else null