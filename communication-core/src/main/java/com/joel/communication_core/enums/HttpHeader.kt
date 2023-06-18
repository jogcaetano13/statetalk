package com.joel.communication_core.enums

enum class HttpHeader(val header: String) {
    ACCEPT("Accept"),
    ACCEPT_CHARSET("Accept-Charset"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCEPT_RANGES("Accept-Ranges"),
    AGE("Age"),
    ALLOW("Allow"),

    // Application-Layer Protocol Negotiation, HTTP/2
    AUTHENTICATION_INFO("Authentication-Info"),
    AUTHORIZATION("Authorization"),
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_LOCATION("Content-Location"),
    CONTENT_RANGE("Content-Range"),
    CONTENT_TYPE("Content-Type");

    companion object {
        fun find(header: String) = values().find { it.header == header }
    }
}
