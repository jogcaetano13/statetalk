package com.joel.communication_core.exceptions

class HttpException(
    val code: Int,
    override val message: String?
): IllegalStateException(message)