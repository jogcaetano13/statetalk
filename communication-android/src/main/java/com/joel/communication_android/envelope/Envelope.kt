package com.joel.communication_android.envelope

@PublishedApi
internal data class Envelope<T>(
    val data: T?
)