package com.joel.jlibtemplate.respositories

import androidx.paging.PagingData
import com.joel.communication.states.ResultState
import com.joel.jlibtemplate.models.Challenge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {

    fun getChallengesPaginated(scope: CoroutineScope): Flow<ResultState<PagingData<Challenge>>>

    fun getChallengesPaginatedOnlyApi(): Flow<ResultState<PagingData<Challenge>>>
}