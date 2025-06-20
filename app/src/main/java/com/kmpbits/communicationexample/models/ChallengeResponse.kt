package com.kmpbits.communicationexample.models

import com.google.gson.annotations.SerializedName

data class ChallengeResponse(
    val totalPages: Int?,
    val totalItems: Int?,
    @SerializedName("data")
    val challenges: List<Challenge>?
)