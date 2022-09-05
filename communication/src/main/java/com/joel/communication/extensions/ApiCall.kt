package com.joel.communication.extensions

import com.joel.communication.client.Client
import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.response.CommunicationResponse
import com.joel.communication.states.AsyncState
import kotlinx.coroutines.withContext

@Suppress("BlockingMethodInNonBlockingContext")
@PublishedApi
internal suspend fun CommunicationRequest.apiCall(
    dispatcher: CommunicationDispatcher
): AsyncState<CommunicationResponse> {
    return withContext(dispatcher.io()) {
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