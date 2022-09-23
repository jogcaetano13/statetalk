package com.joel.jlibtemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.jlibtemplate.respositories.ChallengeRepository

class MainViewModel(
    private val dispatcher: CommunicationDispatcher,
    private val repository: ChallengeRepository
) : ViewModel() {

    fun getChallengesPaginated() = repository.getChallengesPaginated(viewModelScope)
}