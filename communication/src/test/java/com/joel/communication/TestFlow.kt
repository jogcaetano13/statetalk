package com.joel.communication

import com.joel.communication.deserializables.responseListFlow
import com.joel.communication.extensions.call
import com.joel.communication.extensions.mockResponse
import com.joel.communication.extensions.testIn
import com.joel.communication.helpers.successResponse
import com.joel.communication.models.TestModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class TestFlow : BaseTest() {

    @Test
    fun testSuccessListModel() {
        mockResponse {
            body = successResponse()
        }

        runTest {
            val flow = call<List<TestModel>>().responseListFlow().testIn(this)
            println()
        }
    }
}