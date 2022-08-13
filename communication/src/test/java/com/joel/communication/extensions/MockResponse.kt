package com.joel.communication.extensions

import okhttp3.Headers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

val server = MockWebServer()

fun mockResponse(builder: Builder. () -> Unit) = Builder().also(builder).build()

class Builder {
    private val headers = Headers.Builder()

    var code: Int = 200
    var body: String? = null

    fun header(key: String, value: Any) {
        headers.add(key, value.toString())
    }

    fun build() {
        val response = MockResponse()
            .setResponseCode(code)
            .setHeaders(headers.build())

        body?.let {
            response.setBody(it)
        }

        server.enqueue(response)
    }
}