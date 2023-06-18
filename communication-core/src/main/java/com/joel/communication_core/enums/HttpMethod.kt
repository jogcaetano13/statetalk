package com.joel.communication_core.enums

sealed interface HttpMethod {
    object Get : HttpMethod
    object Post : HttpMethod
    object Put : HttpMethod
    object Delete : HttpMethod
    object Patch : HttpMethod
    object Head : HttpMethod
}