package com.joel.communication.extensions

import com.joel.communication.enums.HttpMethod
import com.joel.communication.exceptions.CommunicationsException

internal fun String.toHttpMethod(): HttpMethod = when(this) {
    "GET" -> HttpMethod.GET
    "POST" -> HttpMethod.POST
    "PUT" -> HttpMethod.PUT
    "DELETE" -> HttpMethod.DELETE
    "PATCH" -> HttpMethod.PATCH
    "HEAD" -> HttpMethod.HEAD
    else -> throw CommunicationsException("Invalid HttpMethod: $this")
}