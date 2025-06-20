package com.kmpbits.communication_android.deserializables

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.kmpbits.communication_core.builders.ResponseBuilder
import com.kmpbits.communication_core.deserializables.responseFlow
import com.kmpbits.communication_core.deserializables.responseListFlow
import com.kmpbits.communication_core.deserializables.responseWrappedFlow
import com.kmpbits.communication_core.deserializables.responseWrappedListFlow
import com.kmpbits.communication_core.request.CommunicationRequest
import com.kmpbits.communication_core.states.ResultState

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
