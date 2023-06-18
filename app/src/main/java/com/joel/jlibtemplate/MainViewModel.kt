package com.joel.jlibtemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.joel.communication_android.dispatchers.CommunicationDispatcher
import com.joel.communication_android.states.ResultState
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.respositories.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcher: CommunicationDispatcher,
    private val repository: ChallengeRepository
) : ViewModel() {

    private val _pagingState = MutableStateFlow<PagingData<Challenge>>(PagingData.empty())
    val pagingState by lazy {
        getChallengesPaginated()
        _pagingState.asStateFlow()
    }

    private val _challengesState = MutableStateFlow<ResultState<List<Challenge>>>(ResultState.Empty)
    val challengesState by lazy {
        getChallenges()
        _challengesState.asStateFlow()
    }

    private fun getChallengesPaginated() = viewModelScope.launch(dispatcher.main()) {
        repository.getChallengesPaginated().cachedIn(viewModelScope).collectLatest {
            _pagingState.value = it
        }
    }

    private fun getChallenges() = viewModelScope.launch(dispatcher.main()) {
        repository.getChallenges(dispatcher).collectLatest {
            when(it) {
                ResultState.Empty -> {}
                is ResultState.Error -> _challengesState.value = ResultState.Error(it.error, it.data)
                is ResultState.Loading -> _challengesState.value = ResultState.Loading(it.data)
                is ResultState.Success -> _challengesState.value = ResultState.Success(it.data)
            }
        }
    }
}