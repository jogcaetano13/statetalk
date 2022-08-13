package com.joel.communication.extensions

import com.joel.communication.enums.LogLevel
import okhttp3.logging.HttpLoggingInterceptor

internal fun LogLevel.toHttpLogLevel() = when(this) {
    LogLevel.NONE -> HttpLoggingInterceptor.Level.NONE
    LogLevel.BASIC -> HttpLoggingInterceptor.Level.BASIC
    LogLevel.HEADERS -> HttpLoggingInterceptor.Level.HEADERS
    LogLevel.BODY -> HttpLoggingInterceptor.Level.BODY
}

