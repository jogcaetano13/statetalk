package com.joel.communication.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface CommunicationDispatcher {
    fun main(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}