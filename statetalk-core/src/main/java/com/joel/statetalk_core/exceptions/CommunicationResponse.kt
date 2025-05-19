package com.joel.statetalk_core.exceptions

import com.joel.statetalk_core.enums.ErrorResponseType
import com.joel.statetalk_core.response.CommunicationResponse
import com.joel.statetalk_core.response.ErrorResponse

fun CommunicationResponse.toError(): ErrorResponse {
    return ErrorResponse(code, errorBody, ErrorResponseType.Http)
}