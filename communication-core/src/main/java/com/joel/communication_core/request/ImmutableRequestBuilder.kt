package com.joel.communication_core.request

import com.joel.communication_core.alias.Header

class ImmutableRequestBuilder internal constructor(
    @PublishedApi
    internal val builder: RequestBuilder,
    val preCall: (() -> Unit)?,
    val headers: List<Header>,
    val dateFormat: String
) {

    fun updateParameter(key: String, value: Any) = builder.updateParameter(key, value)
}