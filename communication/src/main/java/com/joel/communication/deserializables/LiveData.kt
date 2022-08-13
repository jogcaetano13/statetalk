@file:Suppress("BlockingMethodInNonBlockingContext")

package com.joel.communication.deserializables

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
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
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
) = liveData {
    responseFlow(responseBuilder).collect {
        emit(it)
    }
}

/**
 * Deserialize the request into a [LiveData] wrapped by the data json object.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseWrappedLiveData(
    crossinline responseBuilder: ResponseBuilder<T>. () -> Unit = {}
) = liveData {
    responseWrappedFlow(responseBuilder).collect {
        emit(it)
    }
}

/**
 * Deserialize the request into a [LiveData] list wrapped by the data json object.
 *
 * This method receives a [ResponseBuilder] as parameter to customize the response.
 *
 * It's offline first and it handles the loading and error, then emits the results into a [ResultState]
 */
inline fun <reified T : Any> CommunicationRequest.responseListLiveData(
    crossinline responseBuilder: ResponseBuilder<List<T>>. () -> Unit = {}
) = liveData {
    responseListFlow(responseBuilder).collect {
        emit(it)
    }
}