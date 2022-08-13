package com.joel.communication

import com.joel.communication.extensions.initClient
import com.joel.communication.extensions.server
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before

abstract class BaseTest {
    @Before
    fun setup() {
        server.start()
        val url = server.url("")
        initClient {
            baseUrl = url.toString()
        }
    }

    @After
    fun close() {
        server.shutdown()
    }

    protected fun setResponse(response: MockResponse) {
        server.enqueue(response)
    }
}