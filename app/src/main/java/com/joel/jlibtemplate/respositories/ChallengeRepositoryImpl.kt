package com.joel.jlibtemplate.respositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.joel.communication.client.Client
import com.joel.communication.deserializables.responseListFlow
import com.joel.communication.deserializables.responsePaginated
import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.states.ResultState
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.room.daos.ChallengeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class ChallengeRepositoryImpl(
    private val client: Client,
    private val dao: ChallengeDao
) : ChallengeRepository {

    override fun getChallenges(dispatcher: CommunicationDispatcher): Flow<ResultState<List<Challenge>>> = client.call {
        path = "api/v1/users/siebenschlaefer/code-challenges/completed"

    }.responseListFlow(dispatcher) {
        onNetworkSuccess {
            dao.replace(it)
        }

        local {
            observe { dao.getChallengesFlow() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getChallengesPaginated(scope: CoroutineScope): Flow<ResultState<PagingData<Challenge>>> = client.call {
        path = "api/v1/users/siebenschlaefer/code-challenges/completed"

    }.responsePaginated {
        cacheScope = scope

        localSource { dao.getChallenges() }
        deleteAll { dao.deleteAll() }
        insertAll { dao.replace(it) }

        firstItemDatabase { dao.getChallenge() }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getChallengesPaginatedOnlyApi(): Flow<ResultState<PagingData<Challenge>>> = client.call {
        path = "api/v1/users/siebenschlaefer/code-challenges/completed"

    }.responsePaginated {
        onlyApiCall = true
    }
}