package com.kmpbits.communication_core.enums

import okhttp3.logging.HttpLoggingInterceptor

sealed interface LogLevel {
    /**
     * No logs.
     */
    object None : LogLevel

    /**
     * Logs request and response lines.
     * <p>
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * }</pre>
     */
    object Basic : LogLevel

    /**
     * Logs request and response lines and their respective headers.
     * <p>
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * }</pre>
     */
    object Headers : LogLevel

    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     * <p>
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * }</pre>
     */
    object Body : LogLevel

    fun fromLevel(): HttpLoggingInterceptor.Level = when(this) {
        Basic -> HttpLoggingInterceptor.Level.BASIC
        Body -> HttpLoggingInterceptor.Level.BODY
        Headers -> HttpLoggingInterceptor.Level.HEADERS
        None -> HttpLoggingInterceptor.Level.NONE
    }
}