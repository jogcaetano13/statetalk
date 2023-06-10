@file:Suppress("BlockingMethodInNonBlockingContext")

package com.joel.communication.deserializables

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.dispatchers.CommunicationDispatcherImpl
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.response.ResponseBuilder
import com.joel.communication.states.ResultState

/**
 * Deserialize the request into a [LiveData].
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseLiveData(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl,
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
) = responseFlow(dispatcher, responseBuilder).asLiveData()

/**
 * Deserialize the request into a [LiveData] wrapped by the data json object.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseWrappedLiveData(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl,
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
) = responseWrappedFlow(dispatcher, responseBuilder).asLiveData()

/**
 * Deserialize the request into a [LiveData] list wrapped by the data json object.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseListLiveData(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl,
    crossinline responseBuilder: ResponseBuilder<List<T>>. () -> Unit = {}
) = responseListFlow(dispatcher, responseBuilder).asLiveData()