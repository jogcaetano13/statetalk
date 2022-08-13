package com.joel.communication.deserializables

import com.joel.communication.envelope.EnvelopeList
import com.joel.communication.extensions.*
import com.joel.communication.models.ErrorResponse
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.response.ResponseBuilder
import com.joel.communication.states.AsyncState

/**
 * Deserialize the request into a [AsyncState].
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * This is a suspend functions, so you need to call it from a coroutine or from another suspend function.
 * The dispatcher is handled by the function, so it can be called from any thread.
 *
 * @return [AsyncState] with success (with data as [T]) or error (with error as [ErrorResponse]).
 */
suspend inline fun <reified T : Any> CommunicationRequest.responseAsync(
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
): AsyncState<T> {
    val response = ResponseBuilder<T>().also(responseBuilder)

    builder.preCall?.invoke()

    val call = apiCall()

    response.post?.invoke()

    return when(call) {
        AsyncState.Empty -> throw IllegalStateException("Empty not used!")
        is AsyncState.Error -> AsyncState.Error(call.error)
        is AsyncState.Success -> {
            val result = call.data.body?.toModel<T>(builder.dateFormat)

            result?.let {
                response.onSuccess?.invoke(it)
                AsyncState.Success(it)
            } ?: AsyncState.Empty

        }
    }
}

/**
 * Deserialize the request into a [AsyncState] list wrapped by the data json object.
 * The list wrapped in json object response should be called "data"
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * This is a suspend functions, so you need to call it from a coroutine or from another suspend function.
 * The dispatcher is handled by the function, so it can be called from any thread.
 *
 * @return [AsyncState] with success (with data as [List] of type [T]) or error (with error as [ErrorResponse]).
 */
suspend inline fun <reified T : Any> CommunicationRequest.responseListAsync(
    crossinline responseBuilder: ResponseBuilder<List<T>>. () -> Unit = {}
): AsyncState<List<T>> {
    val response = ResponseBuilder<List<T>>().also(responseBuilder)

    builder.preCall?.invoke()

    val call = apiCall()

    response.post?.invoke()

    return when(call) {
        AsyncState.Empty -> throw IllegalStateException("Empty not used!")
        is AsyncState.Error -> AsyncState.Error(call.error)
        is AsyncState.Success -> {
            val result = call.data.body?.toList<T>(builder.dateFormat)

            result?.let {
                response.onSuccess?.invoke(it)

                AsyncState.Success(it)

            } ?: AsyncState.Empty
        }
    }
}

/**
 * Deserialize the request into a [AsyncState] wrapped by the data json object.
 * The object wrapped in json object response should be called "data"
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * This is a suspend functions, so you need to call it from a coroutine or from another suspend function.
 * The dispatcher is handled by the function, so it can be called from any thread.
 *
 * @return [AsyncState] with success (with data as [T]) or error (with error as [ErrorResponse]).
 */
suspend inline fun <reified T: Any> CommunicationRequest.responseWrappedAsync(
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
): AsyncState<T> {
    val response = ResponseBuilder<T>().also(responseBuilder)

    builder.preCall?.invoke()

    val call = apiCall()

    response.post?.invoke()

    return when(call) {
        AsyncState.Empty -> throw IllegalStateException("Empty not used!")
        is AsyncState.Error -> AsyncState.Error(call.error)
        is AsyncState.Success -> {
            val result = call.data.body?.toModelWrapped<T>(builder.dateFormat)

            result?.let {
                response.post?.invoke()
                AsyncState.Success(it)
            } ?: AsyncState.Empty
        }
    }
}

@PublishedApi
internal suspend inline fun <reified T : Any> CommunicationRequest.responseStateAsync(): AsyncState<EnvelopeList<T>> {
    return when(val call = apiCall()) {
        AsyncState.Empty -> throw IllegalStateException("Empty not used!")
        is AsyncState.Error -> AsyncState.Error(call.error)
        is AsyncState.Success -> {
            val envelopeList = call.data.body?.toEnvelopeList<T>(builder.dateFormat)
            envelopeList?.let {
                AsyncState.Success(it)
            } ?: AsyncState.Empty
        }
    }
}