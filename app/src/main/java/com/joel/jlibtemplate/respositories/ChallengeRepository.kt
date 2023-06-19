package com.joel.jlibtemplate.respositories

import androidx.paging.PagingData
import com.joel.communication_android.states.ResultState
import com.joel.jlibtemplate.models.Challenge
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {

    fun getChallenges(): Flow<ResultState<List<Challenge>>>

    fun getChallengesPaginated(): Flow<PagingData<Challenge>>

    fun getChallengesPaginatedOnlyApi(): Flow<PagingData<Challenge>>

    suspend fun getChallengesSuspend(): List<Challenge>
}