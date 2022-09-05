@file:Suppress("BlockingMethodInNonBlockingContext")

package com.joel.communication.deserializables

import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.dispatchers.CommunicationDispatcherImpl
import com.joel.communication.enums.ErrorResponseType
import com.joel.communication.exceptions.CommunicationsException
import com.joel.communication.extensions.*
import com.joel.communication.models.ErrorResponse
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.response.ResponseBuilder
import com.joel.communication.states.AsyncState
import com.joel.communication.states.ResultState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
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
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl,
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
) = flowOrCatch((dispatcher)) {
    val response = ResponseBuilder<T>().also(responseBuilder)

    if (response.offlineBuilder?.onlyLocalCall == true && (response.offlineBuilder?.callFlow == null || response.offlineBuilder?.call == null))
        throw CommunicationsException("You must invoke the 'call()' functions to make only offline calls!")

    if (response.offlineBuilder?.call != null && response.offlineBuilder?.callFlow != null)
        throw CommunicationsException("You must only call 'call()' or 'observe()', not both of them!")

    val localCall = withContext(dispatcher.io()) {
        response.offlineBuilder?.call?.invoke() ?: response.offlineBuilder?.callFlow?.invoke()?.first()
    }

    localCall?.let {
        withContext(dispatcher.main()) {
            trySend(ResultState.Success(it))
        }
    }

    if (localCall == null)
        withContext(dispatcher.main()) {
            trySend(ResultState.Loading)
        }

    if (response.offlineBuilder?.onlyLocalCall == true)
        return@flowOrCatch

    builder.preCall?.invoke()

    val call = apiCall(dispatcher)

    response.post?.invoke()

    when(call) {
        is AsyncState.Success -> {
            val result = call.data.body?.toModel<T>(builder.dateFormat)

            withContext(dispatcher.main()) {
                if (result != null) {
                    response.onSuccess?.invoke(result)

                    trySend(ResultState.Success(result))

                } else {
                    trySend(ResultState.Error(ErrorResponse(404, "Response null", ErrorResponseType.EMPTY)))
                }
            }
        }

        is AsyncState.Error -> withContext(dispatcher.main()) { trySend(ResultState.Error(call.error)) }
        AsyncState.Empty -> {}
    }

    response.offlineBuilder?.call?.let {
        it()?.let {
            withContext(dispatcher.main()) {
                ResultState.Success(it)
            }
        }
    } ?: response.offlineBuilder?.callFlow?.invoke()?.collect {
        if (it != null)
            withContext(dispatcher.main()) {
                trySend(ResultState.Success(it))
            }
    }
}

/**
 * Deserialize the request into a [Flow] wrapped by the data json object.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseWrappedFlow(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl,
    crossinline responseBuilder: ResponseBuilder<T>.() -> Unit = {}
) = flowOrCatch(dispatcher) {
    val response = ResponseBuilder<T>().also(responseBuilder)

    if (response.offlineBuilder?.onlyLocalCall == true && (response.offlineBuilder?.callFlow == null || response.offlineBuilder?.call == null))
        throw CommunicationsException("You must invoke the 'call()' functions to make only offline calls!")

    if (response.offlineBuilder?.call != null && response.offlineBuilder?.callFlow != null)
        throw CommunicationsException("You must only call 'call()' or 'observe()', not both of them!")

    val localCall = withContext(dispatcher.io()) {
        response.offlineBuilder?.call?.invoke() ?: response.offlineBuilder?.callFlow?.invoke()?.first()
    }

    localCall?.let {
        withContext(dispatcher.main()) {
            trySend(ResultState.Success(it))
        }
    }

    if (localCall == null)
        withContext(dispatcher.main()) {
            trySend(ResultState.Loading)
        }

    if (response.offlineBuilder?.onlyLocalCall == true)
        return@flowOrCatch

    builder.preCall?.invoke()

    val call = apiCall(dispatcher)

    response.post?.invoke()

    when(call) {
        is AsyncState.Success -> {
            val result = call.data.body?.toModelWrapped<T>(builder.dateFormat)

            withContext(dispatcher.main()) {
                if (result != null) {
                    response.onSuccess?.invoke(result)

                    trySend(ResultState.Success(result))

                } else {
                    trySend(ResultState.Error(ErrorResponse(404, "Response null", ErrorResponseType.EMPTY)))
                }
            }
        }

        is AsyncState.Error -> withContext(dispatcher.main()) { trySend(ResultState.Error(call.error)) }
        AsyncState.Empty -> {}
    }

    response.offlineBuilder?.call?.let {
        it()?.let {
            withContext(dispatcher.main()) {
                ResultState.Success(it)
            }
        }
    } ?: response.offlineBuilder?.callFlow?.invoke()?.collect {
        if (it != null)
            withContext(dispatcher.main()) {
                trySend(ResultState.Success(it))
            }
    }
}

/**
 * Deserialize the request into a [Flow] list wrapped by the data json object.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseListFlow(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl,
    crossinline responseBuilder: ResponseBuilder<List<T>>.() -> Unit = {}
) = flowOrCatch(dispatcher) {
    val response = ResponseBuilder<List<T>>().also(responseBuilder)

    if (response.offlineBuilder?.onlyLocalCall == true && (response.offlineBuilder?.callFlow == null || response.offlineBuilder?.call == null))
        throw CommunicationsException("You must invoke the 'call()' functions to make only offline calls!")

    if (response.offlineBuilder?.call != null && response.offlineBuilder?.callFlow != null)
        throw CommunicationsException("You must only call 'call()' or 'observe()', not both of them!")

    val localCall = withContext(dispatcher.io()) {
        response.offlineBuilder?.call?.invoke() ?: response.offlineBuilder?.callFlow?.invoke()?.first()
    }

    if (localCall.isNullOrEmpty().not())
        withContext(dispatcher.main()) {
            trySend(ResultState.Success(localCall!!))
        }

    if (localCall.isNullOrEmpty())
        withContext(dispatcher.main()) {
            trySend(ResultState.Loading)
        }

    if (response.offlineBuilder?.onlyLocalCall == true)
        return@flowOrCatch

    builder.preCall?.invoke()

    val call = apiCall(dispatcher)

    response.post?.invoke()

    when(call) {
        is AsyncState.Success -> {
            val result = call.data.body?.toList<T>(builder.dateFormat)

            withContext(dispatcher.main()) {
                if (result.isNullOrEmpty().not()) {
                    response.onSuccess?.invoke(result!!)

                    trySend(ResultState.Success(result!!))

                } else {
                    trySend(ResultState.Error(ErrorResponse(404, "Response empty", ErrorResponseType.EMPTY)))
                }
            }
        }

        is AsyncState.Error -> withContext(dispatcher.main()) { trySend(ResultState.Error(call.error)) }
        AsyncState.Empty -> {}
    }

    response.offlineBuilder?.call?.let {
        it()?.let {
            withContext(dispatcher.main()) {
                ResultState.Success(it)
            }
        }
    } ?: response.offlineBuilder?.callFlow?.invoke()?.collect {
        if (it != null)
            withContext(dispatcher.main()) {
                trySend(ResultState.Success(it))
            }
    }
}

@PublishedApi
internal fun <T : Any> flowOrCatch(
    communicationDispatcher: CommunicationDispatcher,
    block: suspend ProducerScope<ResultState<T>>.() -> Unit
) = channelFlow {
    block(this)
}.flowOn(communicationDispatcher.default()).catch {
    it.printStackTrace()
    emit(ResultState.Error(it.apiError))
}