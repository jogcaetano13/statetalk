package com.kmpbits.communication_core.extensions

import com.kmpbits.communication_core.enums.HttpMethod
import com.kmpbits.communication_core.exceptions.CommunicationException

internal fun String.toHttpMethod(): HttpMethod = when(this) {
    "GET" -> HttpMethod.Get
    "POST" -> HttpMethod.Post
    "PUT" -> HttpMethod.Put
    "DELETE" -> HttpMethod.Delete
    "PATCH" -> HttpMethod.Patch
    "HEAD" -> HttpMethod.Head
    else -> throw CommunicationException("Invalid HttpMethod: $this")
}

internal fun HttpMethod.toName() = when(this) {
    HttpMethod.Delete -> "DELETE"
    HttpMethod.Get -> "GET"
    HttpMethod.Head -> "HEAD"
    HttpMethod.Patch -> "PATCH"
    HttpMethod.Post -> "POST"
    HttpMethod.Put -> "PUT"
}