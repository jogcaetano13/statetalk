package com.joel.jlibtemplate.respositories

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.joel.communication_android.deserializables.responseListFlow
import com.joel.communication_android.deserializables.responsePaginated
import com.joel.communication_android.states.ResultState
import com.joel.communication_core.alias.Header
import com.joel.communication_core.client.Client
import com.joel.communication_core.enums.HttpHeader
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.room.daos.ChallengeDao
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

        val headers = request.headers
        print(headers)

        Log.d("Paginated header:", "getChallenges: ${request.headers}")

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

        Log.d("Paginated header:", "getChallengesPaginatedOnlyApi: ${request.headers}")

        return request.responsePaginated {
            onlyApiCall = true
        }
    }
}