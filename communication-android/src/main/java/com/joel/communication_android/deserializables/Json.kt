package com.joel.communication_android.deserializables

import com.joel.communication_android.extensions.toJsonObject
import com.joel.communication_core.enums.ErrorResponseType
import com.joel.communication_core.exceptions.toError
import com.joel.communication_core.request.CommunicationRequest
import com.joel.communication_core.response.ErrorResponse
import com.joel.communication_core.states.AsyncState
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
suspend fun CommunicationRequest.responseJsonObject() : AsyncState<JSONObject> {
    val callResponse = response()

    return if (callResponse.isSuccess) {
        val json = callResponse.body?.string()?.toJsonObject()

        json?.let {
            AsyncState.Success(it)
        } ?: AsyncState.Error(ErrorResponse(404, "Not found", ErrorResponseType.Empty))

    } else {
        AsyncState.Error(callResponse.toError())
    }
}