package com.joel.communication_android.extensions

import com.joel.communication_core.enums.ErrorResponseType
import com.joel.communication_core.response.CommunicationResponse
import com.joel.communication_core.response.ErrorResponse

fun CommunicationResponse.toError(): ErrorResponse {
    return ErrorResponse(code, errorBody, ErrorResponseType.Http)
}