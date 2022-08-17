package com.joel.jlibtemplate.respositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.joel.communication.client.Client
import com.joel.communication.deserializables.responsePaginated
import com.joel.communication.states.ResultState
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.room.daos.ChallengeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class ChallengeRepositoryImpl(
    private val client: Client,
    private val dao: ChallengeDao
) : ChallengeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getChallengesPaginated(scope: CoroutineScope): Flow<ResultState<PagingData<Challenge>>> = client.call {
        path = "api/v1/users/siebenschlaefer/code-challenges/complete"

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