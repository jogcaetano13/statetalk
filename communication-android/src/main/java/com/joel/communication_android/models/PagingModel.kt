package com.joel.communication_android.models

abstract class PagingModel {
    internal var page: Int = 1
    internal var lastUpdatedTimestamp: Long? = null
}