package com.joel.communication.builders

import com.joel.communication.annotations.CommunicationsMarker
import com.joel.communication.enums.LogLevel

@CommunicationsMarker
class LogBuilder {

    /**
     * A class that logs request and response information.
     *
     */
    var logLevel: LogLevel = LogLevel.BODY
}