package com.joel.communication.builders

import com.joel.communication.annotations.CommunicationsMarker
import com.joel.communication.valueclasses.Duration

@CommunicationsMarker
class TimeoutBuilder {

    /**
     * Sets the default connect timeout for new connections. A value of 0 means no timeout.
     *
     * The connect timeout is applied when connecting a TCP socket to the target host. The default
     * value is 15 seconds.
     */
    var connectionTimeout: Duration = Duration.seconds(15)

    /**
     * Sets the default read timeout for new connections. A value of 0 means no timeout.
     *
     * The read timeout is applied to both the TCP socket and for individual read IO operations.
     *
     * The default value is 15 seconds.
     */
    var readTimeout: Duration = Duration.seconds(15)

    /**
     * Sets the default write timeout for new connections. A value of 0 means no timeout.
     *
     * The write timeout is applied for individual write IO operations. The default value is 15
     * seconds.
     */
    var writeTimeout: Duration = Duration.seconds(15)
}