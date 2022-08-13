package com.joel.communication.extensions

import com.joel.communication.client.Client
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.response.CommunicationResponse
import com.joel.communication.states.AsyncState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("BlockingMethodInNonBlockingContext")
@PublishedApi
internal suspend fun CommunicationRequest.apiCall(): AsyncState<CommunicationResponse> {
    return withContext(Dispatchers.IO) {
        try {
            val response = Client.instance.call(this@apiCall).execute()

            if (response.isSuccessful)
                AsyncState.Success(CommunicationResponse(
                    response.code,
                    headers,
                    response.body?.string()
                ))
            else
                AsyncState.Error(response.getApiError())

        } catch (e: Exception) {
            AsyncState.Error(e.apiError)
        }
    }
}