package com.joel.communication_android.deserializables

import com.joel.communication_android.builders.ResponseBuilder
import com.joel.communication_android.extensions.toEnvelopeList
import com.joel.communication_android.extensions.toError
import com.joel.communication_android.extensions.toList
import com.joel.communication_android.extensions.toModel
import com.joel.communication_android.extensions.toModelWrapped
import com.joel.communication_android.states.ResultState
import com.joel.communication_core.enums.ErrorResponseType
import com.joel.communication_core.exceptions.CommunicationsException
import com.joel.communication_core.extensions.apiError
import com.joel.communication_core.request.CommunicationRequest
import com.joel.communication_core.response.CommunicationResponse
import com.joel.communication_core.response.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author joelcaetano
 * Created 28/11/2021 at 19:12
 */

/**
 * Deserialize the request into a [Flow].
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseFlow(
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
): Flow<ResultState<T>> {
    return toFlow(
        responseBuilder = responseBuilder,
        deserializeBlock = { it.toModel<T>(immutableRequestBuilder.dateFormat) }
    )
}

/**
 * Deserialize the request into a [Flow] wrapped by the data json object.
 * The list wrapped in json object response should be called "data".
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseWrappedFlow(
    crossinline responseBuilder: ResponseBuilder<T>.() -> Unit = {}
): Flow<ResultState<T>> {
    return toFlow(
        responseBuilder = responseBuilder,
        deserializeBlock = { it.toModelWrapped<T>(immutableRequestBuilder.dateFormat) }
    )
}

/**
 * Deserialize the request into a [Flow] list.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseListFlow(
    crossinline responseBuilder: ResponseBuilder<List<T>>.() -> Unit = {}
): Flow<ResultState<List<T>>> {
    return toFlow(
        responseBuilder = responseBuilder,
        deserializeBlock = { it.toList(immutableRequestBuilder.dateFormat) }
    )
}

/**
 * Deserialize the request into a [Flow] list wrapped by the data json object.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseWrappedListFlow(
    crossinline responseBuilder: ResponseBuilder<List<T>>.() -> Unit = {}
): Flow<ResultState<List<T>>> {
    return toFlow(
        responseBuilder = responseBuilder,
        deserializeBlock = { it.toEnvelopeList<T>(immutableRequestBuilder.dateFormat).data }
    )
}

@PublishedApi
internal inline fun <reified T : Any> CommunicationRequest.toFlow(
    crossinline responseBuilder: ResponseBuilder<T>.() -> Unit,
    crossinline deserializeBlock: (CommunicationResponse) -> T?
): Flow<ResultState<T>> = callbackFlow {
    val response = ResponseBuilder<T>().also(responseBuilder)

    if (response.offlineBuilder?.onlyLocalCall == true && (response.offlineBuilder?.callFlow == null && response.offlineBuilder?.call == null))
        throw CommunicationsException("You must invoke the 'call()' functions to make only offline calls!")

    val localCall = withContext(Dispatchers.IO) {
        response.offlineBuilder?.call?.invoke() ?: response.offlineBuilder?.callFlow?.invoke()?.first()
    }

    if (response.offlineBuilder?.onlyLocalCall == true) {
        localCall?.let {
            send(ResultState.Success(it))
        } ?: run {
            send(ResultState.Error(ErrorResponse(404, "Empty", ErrorResponseType.Empty)))
        }

        return@callbackFlow
    }

    send(ResultState.Loading(localCall))

    immutableRequestBuilder.preCall?.invoke()

    try {
        val callResponse = response()

        response.post?.invoke()

        if (callResponse.isSuccess) {
            val result = deserializeBlock(callResponse)

            result?.let {
                withContext(Dispatchers.IO) { response.onNetworkSuccess?.invoke(result) }

                if (response.offlineBuilder?.call == null || response.offlineBuilder?.callFlow == null) {
                    send(ResultState.Success(result))

                } else {
                    // No-op. It will update from local database
                }
            } ?: run {
                send(
                    ResultState.Error(
                        error = ErrorResponse(
                            404,
                            "Response null",
                            ErrorResponseType.Empty
                        ),
                        data = localCall
                    ))
            }

        } else {
            send(
                ResultState.Error(
                    error = callResponse.toError(),
                    data = localCall
                ))
        }

        var job: Job? = null

        response.offlineBuilder?.call?.let {
            it()?.let {
                ResultState.Success(it)
            }
        } ?: run {
            job = launch {
                response.offlineBuilder?.callFlow?.invoke()?.collect {
                    it?.let {
                        send(ResultState.Success(it))
                    }
                }
            }
        }

        awaitClose {
            job?.cancel()
        }

    } catch (e: Exception) {
        send(
            ResultState.Error(
                error = e.apiError,
                data = localCall
            ))
    }
}