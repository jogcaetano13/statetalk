package com.kmpbits.communicationexample.respositories

import androidx.paging.PagingData
import com.kmpbits.communicationexample.models.Challenge
import com.kmpbits.communication_core.states.ResultState
import kotlinx.coroutines.flow.Flow

interface ChallengeRepository {

    fun getChallenges(): Flow<ResultState<List<Challenge>>>

    fun getChallengesPaginated(): Flow<PagingData<Challenge>>

    fun getChallengesPaginatedOnlyApi(): Flow<PagingData<Challenge>>

    suspend fun getChallengesSuspend(): List<Challenge>
}