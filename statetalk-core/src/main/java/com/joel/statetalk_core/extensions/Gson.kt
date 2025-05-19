package com.joel.statetalk_core.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.joel.statetalk_core.envelope.Envelope
import com.joel.statetalk_core.envelope.EnvelopeList
import com.joel.statetalk_core.response.CommunicationResponse

internal fun <T> T.toJson(datePattern: String): String {
    val type = object : TypeToken<T>() {}.type
    return createGson(datePattern).toJson(this, type)
}

@PublishedApi
internal inline fun <reified T> CommunicationResponse.toList(datePattern: String): List<T> {
    val type = object : TypeToken<EnvelopeList<T>>() {}.type
    return createGson(datePattern).fromJson(body?.string(), type)
}

@PublishedApi
internal inline fun <reified T> CommunicationResponse.toModelWrapped(datePattern: String): T? {
    val type = object : TypeToken<Envelope<T>>() {}.type
    return createGson(datePattern).fromJson<Envelope<T>>(body?.string(), type).data
}

@PublishedApi
internal inline fun <reified T> CommunicationResponse.toModel(datePattern: String): T {
    val type = object : TypeToken<T>() {}.type
    return createGson(datePattern).fromJson(body?.string(), type)
}

@PublishedApi
internal inline fun <reified T> CommunicationResponse.toEnvelopeList(datePattern: String): EnvelopeList<T> {
    val type = object : TypeToken<EnvelopeList<T>>() {}.type
    return createGson(datePattern).fromJson(body?.string(), type)
}

@PublishedApi
internal fun createGson(datePattern: String): Gson = GsonBuilder()
    .setDateFormat(datePattern)
    .create()