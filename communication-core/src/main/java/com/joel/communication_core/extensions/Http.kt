package com.joel.communication_core.extensions

import com.joel.communication_core.client.Client
import com.joel.communication_core.client.ClientBuilder

/**
 * Initiate and retrieve the [Call] instance with the [ClientBuilder]
 *
 * @return The client to start a new request
 */
fun communicationClient(builder: ClientBuilder. () -> Unit): Client {
    return ClientBuilder().also(builder).build()
}