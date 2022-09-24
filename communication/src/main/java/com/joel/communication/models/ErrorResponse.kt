package com.joel.communication.models

import com.joel.communication.enums.ErrorResponseType

data class ErrorResponse(
    val code: Int,
    val errorBody: String?,
    val type: ErrorResponseType
)
