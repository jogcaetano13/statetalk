package com.joel.communication_android.deserializables

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.joel.communication_android.builders.ResponseBuilder
import com.joel.communication_android.states.ResultState
import com.joel.communication_core.request.CommunicationRequest

/**
 * Deserialize the request into a [LiveData].
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseLiveData(
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
) = responseFlow(responseBuilder).asLiveData()

/**
 * Deserialize the request into a [LiveData] wrapped by the data json object.
 * The list wrapped in json object response should be called "data"
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseWrappedLiveData(
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
) = responseWrappedFlow(responseBuilder).asLiveData()

/**
 * Deserialize the request into a [LiveData] list.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseListLiveData(
    crossinline responseBuilder: ResponseBuilder<List<T>>. () -> Unit = {}
) = responseListFlow(responseBuilder).asLiveData()

/**
 * Deserialize the request into a [LiveData] list wrapped by the data json object.
 * The list wrapped in json object response should be called "data"
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseWrappedListLiveData(
    crossinline responseBuilder: ResponseBuilder<List<T>>. () -> Unit = {}
) = responseWrappedListFlow(responseBuilder).asLiveData()
