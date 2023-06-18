package com.joel.communication_core.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

internal fun <T> T.toJson(datePattern: String): String {
    val type = object : TypeToken<T>() {}.type
    return createGson(datePattern).toJson(this, type)
}

@PublishedApi
internal fun createGson(datePattern: String): Gson = GsonBuilder()
    .setDateFormat(datePattern)
    .create()