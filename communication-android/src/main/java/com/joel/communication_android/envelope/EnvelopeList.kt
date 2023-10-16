package com.joel.communication_android.envelope

import com.google.gson.annotations.SerializedName

data class EnvelopeList<T> internal constructor(
    @SerializedName("data")
    val data: List<T>
)
