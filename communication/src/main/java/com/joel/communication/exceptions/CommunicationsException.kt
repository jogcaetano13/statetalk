package com.joel.communication.exceptions

class CommunicationsException(override val message: String?) : IllegalStateException(message)