package com.joel.statetalk_core.exceptions

import com.joel.statetalk_core.enums.ErrorResponseType
import com.joel.statetalk_core.response.ErrorResponse
import com.joel.statetalk_core.response.StateTalkResponse

fun StateTalkResponse.toError(): ErrorResponse {
    return ErrorResponse(code, errorBody, ErrorResponseType.Http)
}