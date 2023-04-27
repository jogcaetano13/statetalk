package com.joel.jlibtemplate.mvvm.xml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.respositories.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class XmlExampleViewModel(
    private val dispatcher: CommunicationDispatcher,
    private val repository: ChallengeRepository
) : ViewModel() {

    private val _pagingState = MutableStateFlow<PagingData<Challenge>>(PagingData.empty())
    val pagingState by lazy {
        getChallengesPaginated()
        _pagingState.asStateFlow()
    }

    private fun getChallengesPaginated() = viewModelScope.launch(dispatcher.main()) {
        repository.getChallengesPaginated().cachedIn(viewModelScope).collectLatest {
            _pagingState.value = it
        }
    }
}