package com.kmpbits.communication_core.response

import com.kmpbits.communication_core.enums.ErrorResponseType

data class ErrorResponse(
    val code: Int,
    val errorBody: String?,
    val type: ErrorResponseType
)
