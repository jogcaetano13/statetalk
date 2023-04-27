package com.joel.jlibtemplate.mvvm.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.states.ResultState
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.respositories.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ComposeExampleViewModel(
    private val dispatcher: CommunicationDispatcher,
    private val repository: ChallengeRepository
) : ViewModel() {

    private val _challengesState = MutableStateFlow<ResultState<List<Challenge>>>(ResultState.Empty)
    val challengesState by lazy {
        getChallenges()
        _challengesState.asStateFlow()
    }

    private fun getChallenges() = viewModelScope.launch(dispatcher.main()) {
        repository.getChallenges(dispatcher).collectLatest {
            _challengesState.value = it
        }
    }
}