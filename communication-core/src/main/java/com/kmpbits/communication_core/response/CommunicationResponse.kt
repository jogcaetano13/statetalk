package com.kmpbits.communication_core.response

import com.kmpbits.communication_core.alias.Header
import okhttp3.ResponseBody

class CommunicationResponse internal constructor(
    val code: Int,
    val headers: List<Header>,
    val body: ResponseBody?,
    val errorBody: String?
) {
    val isSuccess: Boolean
        get() = code in (200..299) && body != null
}