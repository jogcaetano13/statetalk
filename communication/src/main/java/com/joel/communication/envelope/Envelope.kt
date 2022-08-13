package com.joel.communication.envelope

@PublishedApi
internal data class Envelope<T>(
    val data: T?
)