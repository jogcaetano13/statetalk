package com.joel.communication.extensions

import com.joel.communication.helpers.TestFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.TestScope

@OptIn(InternalCoroutinesApi::class)
@FlowPreview
@ExperimentalCoroutinesApi
fun <T> Flow<T>.test(): TestFlow<T> =
    TestFlow(this)

/**
 * Tests a [Flow] by creating and returning a [TestFlow] which caches all value
 * emissions, error and completion.
 *
 * The [TestFlow] is also launched inside the [scope].
 */
@InternalCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
fun <T> Flow<T>.testIn(scope: TestScope): TestFlow<T> {
    val testFlow = TestFlow(this)
    testFlow.launchIn(scope)
    return testFlow
}