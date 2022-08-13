package com.joel.communication.models

import com.joel.communication.enums.ErrorResponseType

data class ErrorResponse(
    val code: Int,
    val error: String?,
    val type: ErrorResponseType
)
