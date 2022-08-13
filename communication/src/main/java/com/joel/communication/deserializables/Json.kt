package com.joel.communication.deserializables

import com.joel.communication.extensions.apiCall
import com.joel.communication.extensions.toJsonObject
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.states.AsyncState
import org.json.JSONObject

/**
 * @author joelcaetano
 * Created 28/11/2021 at 19:26
 */

/**
 * Deserialize the request to a [JSONObject] wrapped by [AsyncState]-
 *
 * @return [AsyncState]
 */
suspend fun CommunicationRequest.responseJsonObject() : AsyncState<JSONObject> = when(val call = apiCall()) {
    AsyncState.Empty -> throw IllegalStateException("Empty not used!")
    is AsyncState.Error -> AsyncState.Error(call.error)
    is AsyncState.Success -> {
        val json = call.data.body?.toJsonObject()

        json?.let {
            AsyncState.Success(it)
        } ?: AsyncState.Empty
    }
}