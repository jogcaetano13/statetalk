package com.joel.statetalk_core.envelope

data class Envelope<T> internal constructor(
    val data: T?
)