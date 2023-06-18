package com.joel.communication_android.deserializables

import com.joel.communication_android.dispatchers.CommunicationDispatcher
import com.joel.communication_android.dispatchers.CommunicationDispatcherImpl
import com.joel.communication_android.enums.ErrorResponseType
import com.joel.communication_android.extensions.apiCall
import com.joel.communication_android.extensions.toJsonObject
import com.joel.communication_android.request.CommunicationRequest
import com.joel.communication_android.states.AsyncState
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
        val json = call.data.body?.string()?.toJsonObject()

        withContext(dispatcher.main()) {
            json?.let {
                AsyncState.Success(it)
            } ?: AsyncState.Error(ErrorResponse(404, "Not found", ErrorResponseType.EMPTY))
        }
    }
}