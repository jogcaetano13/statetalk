package com.joel.communication_core.request

import com.joel.communication_core.alias.Header

class ImmutableRequestBuilder internal constructor(
    val preCall: (() -> Unit)?,
    val headers: List<Header>,
    val dateFormat: String
)

fun RequestBuilder.toImmutableBuilder() = ImmutableRequestBuilder(
    preCall, headers, dateFormat
)