package com.joel.communication.deserializables

import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.dispatchers.CommunicationDispatcherImpl
import com.joel.communication.enums.ErrorResponseType
import com.joel.communication.extensions.apiCall
import com.joel.communication.extensions.toJsonObject
import com.joel.communication.models.ErrorResponse
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.states.AsyncState
import kotlinx.coroutines.withContext
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
suspend fun CommunicationRequest.responseJsonObject(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl
) : AsyncState<JSONObject> = when(val call = apiCall(dispatcher)) {
    AsyncState.Empty -> throw IllegalStateException("Empty not used!")
    is AsyncState.Error -> AsyncState.Error(call.error)
    is AsyncState.Success -> {
        val json = call.data.body?.toJsonObject()

        withContext(dispatcher.main()) {
            json?.let {
                AsyncState.Success(it)
            } ?: AsyncState.Error(ErrorResponse(404, "Not found", ErrorResponseType.EMPTY))
        }
    }
}