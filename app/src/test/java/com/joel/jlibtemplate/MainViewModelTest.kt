package com.joel.jlibtemplate

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.joel.communication_android.dispatchers.CommunicationDispatcher
import com.joel.communication_core.states.ResultState
import com.joel.jlibtemplate.respositories.ChallengeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @Mock
    private lateinit var dispatcher: CommunicationDispatcher

    @Mock
    private lateinit var repository: ChallengeRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(dispatcher, repository)

        given(dispatcher.main()).willReturn(UnconfinedTestDispatcher())
        given(dispatcher.default()).willReturn(UnconfinedTestDispatcher())
        given(dispatcher.io()).willReturn(UnconfinedTestDispatcher())
    }

    @Test
    fun `given challenges, then return success`() = runTest {
        given(repository.getChallenges(dispatcher)).willReturn(flowOf(ResultState.Success(listOf())))

        viewModel.getChallenges().test {
            val state = awaitItem()
            awaitComplete()

            assertThat(state is ResultState.Success).isTrue()
        }
    }
}