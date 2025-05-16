package com.joel.communication_core.envelope

data class Envelope<T> internal constructor(
    val data: T?
)