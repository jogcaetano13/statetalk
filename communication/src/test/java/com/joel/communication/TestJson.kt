package com.joel.communication


import com.joel.communication.client.Client
import com.joel.communication.deserializables.responseStringAsync
import com.joel.communication.extensions.call
import com.joel.communication.extensions.mockResponse
import com.joel.communication.extensions.toJsonObject
import com.joel.communication.extensions.toModel
import com.joel.communication.helpers.errorResponse
import com.joel.communication.helpers.successResponse
import com.joel.communication.models.ErrorResponse
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author joelcaetano
 * Created 28/11/2021 at 19:39
 */
class TestJson : BaseTest() {

    @Test
    fun testGetStringError() {
        mockResponse {
            code = 500
            body = errorResponse()
        }

        runBlocking {
            val response = call<String>().responseStringAsync()
            assertEquals(errorResponse(), response)
        }
    }

    @Test
    fun testGetJsonObjectSuccess() {
        mockResponse {
            body = successResponse()
        }

        runBlocking {
            val request = call<JSONObject>()
            val response = Client.instance.call(request).execute()

            val expected = JSONObject(successResponse()).toString().trim()
            val body = response.body?.string()?.toJsonObject().toString().trim()

            assertEquals(expected, body)
        }
    }

    @Test
    fun testGetJsonObjectError() {
        mockResponse {
            code = 500
            body = errorResponse()
        }

        runBlocking {
            val request = call<JSONObject>()
            val response = Client.instance.call(request).execute()

            val expected = JSONObject(errorResponse()).toString().trim().toModel<ErrorResponse>()
            val body = response.body?.string()?.toModel<ErrorResponse>()

            assertEquals(expected, body)
        }
    }
}