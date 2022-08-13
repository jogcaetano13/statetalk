package com.joel.communication.extensions

import com.joel.communication.builders.ClientBuilder
import com.joel.communication.calls.Call
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.request.RequestBuilder

/**
 * Initiate and retrieve the [Call] instance with the [ClientBuilder]
 *
 * @return The client to start a new request
 */
fun communicationClient(builder: ClientBuilder.() -> Unit) =
    ClientBuilder.instance.also(builder).build()