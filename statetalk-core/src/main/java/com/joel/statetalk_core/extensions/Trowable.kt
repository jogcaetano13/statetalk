package com.joel.statetalk_core.extensions

import com.joel.statetalk_core.enums.ErrorResponseType
import com.joel.statetalk_core.response.ErrorResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val Throwable.apiError
    get() = when(this) {
        is SocketTimeoutException -> ErrorResponse(408, "Timeout", ErrorResponseType.Timeout)
        is UnknownHostException -> ErrorResponse(500, "No Internet", ErrorResponseType.InternetConnection)
        else -> ErrorResponse(500, message, ErrorResponseType.Unknown)
    }