package com.joel.jlibtemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joel.jlibtemplate.respositories.ChallengeRepository

class MainViewModel(
    private val repository: ChallengeRepository
) : ViewModel() {

    fun getChallenges() = repository.getChallengesPaginated(viewModelScope)
}