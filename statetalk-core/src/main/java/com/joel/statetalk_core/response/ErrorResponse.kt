package com.joel.statetalk_core.response

import com.joel.statetalk_core.enums.ErrorResponseType

data class ErrorResponse(
    val code: Int,
    val errorBody: String?,
    val type: ErrorResponseType
)
