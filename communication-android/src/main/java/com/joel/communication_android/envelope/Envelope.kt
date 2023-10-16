package com.joel.communication_android.envelope

data class Envelope<T> internal constructor(
    val data: T?
)