package com.kmpbits.communication_core.exceptions

import com.kmpbits.communication_core.enums.ErrorResponseType
import com.kmpbits.communication_core.response.ErrorResponse
import com.kmpbits.communication_core.response.CommunicationResponse

fun CommunicationResponse.toError(): ErrorResponse {
    return ErrorResponse(code, errorBody, ErrorResponseType.Http)
}