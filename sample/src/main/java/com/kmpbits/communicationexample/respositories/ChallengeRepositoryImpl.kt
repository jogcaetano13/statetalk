package com.kmpbits.communicationexample.respositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.kmpbits.communicationexample.models.Challenge
import com.kmpbits.communicationexample.room.daos.ChallengeDao
import com.kmpbits.communication_core.alias.Header
import com.kmpbits.communication_core.client.Client
import com.kmpbits.communication_core.deserializables.responseListFlow
import com.kmpbits.communication_core.deserializables.responseToModel
import com.kmpbits.communication_core.enums.HttpHeader
import com.kmpbits.communication_core.states.ResultState
import com.kmpbits.communication_paging.deserializables.responsePaginated
import kotlinx.coroutines.flow.Flow

class ChallengeRepositoryImpl(
    private val client: Client,
    private val dao: ChallengeDao
) : ChallengeRepository {

    override fun getChallenges(): Flow<ResultState<List<Challenge>>> {
        val request = client.call {
            path = "api/v1/users/siebenschlaefer/code-challenges/completed"

            header(Header(HttpHeader.custom("custom_header_request"), "Custom header"))
        }

        return request.responseListFlow {

        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getChallengesPaginated(): Flow<PagingData<Challenge>> = client.call {
        path = "api/v1/users/siebenschlaefer/code-challenges/completed"

    }.responsePaginated {
        localSource { dao.getChallenges() }
        deleteAll { dao.deleteAll() }
        insertAll { dao.replace(it) }

        firstItemDatabase { dao.getChallenge() }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getChallengesPaginatedOnlyApi(): Flow<PagingData<Challenge>> {
        val request = client.call {
            path = "api/v1/users/siebenschlaefer/code-challenges/completed"

        }

        return request.responsePaginated {
            onlyApiCall = true
        }
    }

    override suspend fun getChallengesSuspend(): List<Challenge> = client.call {
        path = "api/v1/users/siebenschlaefer/code-challenges/completed"

    }.responseToModel()
}