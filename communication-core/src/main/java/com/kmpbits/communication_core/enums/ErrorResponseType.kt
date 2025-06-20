package com.kmpbits.communication_core.enums

sealed interface ErrorResponseType {
    object Http : ErrorResponseType
    object Empty : ErrorResponseType
    object Timeout : ErrorResponseType
    object ServiceUnavailable : ErrorResponseType
    object Unknown : ErrorResponseType
    object InternetConnection : ErrorResponseType
}