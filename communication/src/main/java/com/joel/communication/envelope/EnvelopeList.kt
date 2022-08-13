package com.joel.communication.envelope

import com.google.gson.annotations.SerializedName

@PublishedApi
internal data class EnvelopeList<T>(
    @SerializedName("data")
    val data: List<T>
)
