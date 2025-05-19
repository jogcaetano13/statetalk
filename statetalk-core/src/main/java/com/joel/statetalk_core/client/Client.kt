package com.joel.statetalk_core.client

import com.joel.statetalk_core.request.RequestBuilder
import com.joel.statetalk_core.request.StateTalkRequest

interface Client {

    /**
     * This method should be called for every request.
     *
     * @param builder Have the customization of the request like the path and headers.
     * @return A [StateTalkRequest] that can be used to deserialize the response.
     */
    fun call(builder: RequestBuilder. () -> Unit = {}): StateTalkRequest
}