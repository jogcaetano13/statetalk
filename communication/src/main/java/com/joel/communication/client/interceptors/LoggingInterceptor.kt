package com.joel.communication.client.interceptors

import com.joel.communication.client.interceptors.LoggingInterceptor.Logger
import com.joel.communication.enums.LogLevel
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.platform.Platform
import okio.Buffer
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

internal class LoggingInterceptor: Interceptor {

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }

    private val logger: Logger = Logger.DEFAULT_LOGGER

    var level: LogLevel = LogLevel.None

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (level == LogLevel.None) {
            return chain.proceed(request)
        }

        val logBody = level == LogLevel.Body
        val logHeaders = logBody || level == LogLevel.Headers

        val requestBody = request.body
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = connection?.protocol() ?: Protocol.HTTP_1_1
        var requestStartMessage: String =
            "--> " + request.method + ' ' + request.url + ' ' + protocol
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        logger.log(requestStartMessage)

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType())
                }
                if (requestBody.contentLength() != -1L) {
                    logger.log("Content-Length: " + requestBody.contentLength())
                }
            }
            val headers = request.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(
                        name,
                        ignoreCase = true
                    ) && !"Content-Length".equals(name, ignoreCase = true)
                ) {
                    logger.log(name + ": " + headers.value(i))
                }
                i++
            }
            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method)
            } else if (request.headers.bodyEncoded()) {
                logger.log("--> END " + request.method + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)
                var charset: Charset = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)!!
                }
                logger.log("")
                if (buffer.isPlaintext()) {
                    logger.log(buffer.readString(charset))
                    logger.log(
                        "--> END " + request.method
                                + " (" + requestBody.contentLength() + "-byte body)"
                    )
                } else {
                    logger.log(
                        ("--> END " + request.method + " (binary "
                                + requestBody.contentLength() + "-byte body omitted)")
                    )
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        logger.log(
            ("<-- " + response.code + ' ' + response.message + ' '
                    + response.request.url + " (" + tookMs + "ms" + (if (!logHeaders) (", "
                    + bodySize + " body") else "") + ')')
        )

        if (logHeaders) {
            val headers = response.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i))
                i++
            }
            if (response.headers.bodyEncoded()) {
                logger.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                var charset: Charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)!!
                }
                if (!buffer.isPlaintext()) {
                    logger.log("")
                    logger.log("<-- END HTTP (binary " + buffer.size + "-byte body omitted)")
                    return response
                }
                if (contentLength != 0L) {
                    logger.log("")
                    logger.log(buffer.clone().readString((charset)))
                }
                logger.log("<-- END HTTP (" + buffer.size + "-byte body)")
            }
        }

        return response
    }

    fun interface Logger {
        fun log(message: String)

        companion object {
            val DEFAULT_LOGGER = Logger {
                Platform.get().log(it)
            }
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private fun Buffer.isPlaintext(): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (size < 64) size else 64
            copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false // Truncated UTF-8 sequence.
        }
    }

    private fun Headers.bodyEncoded(): Boolean {
        val contentEncoding = this["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }
}