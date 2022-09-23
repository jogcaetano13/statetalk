package com.joel.communication.response

import com.joel.communication.alias.Header

data class CommunicationResponse internal constructor(
    val code: Int,
    val headers: List<Header>,
    val body: String?
) {
    val isSuccess: Boolean
        get() = code in (200..299)
}
