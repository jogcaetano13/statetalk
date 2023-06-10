package com.joel.communication.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.joel.communication.envelope.Envelope
import com.joel.communication.envelope.EnvelopeList

@PublishedApi
internal inline fun <reified T> String.toList(datePattern: String): List<T> {
    val type = object : TypeToken<EnvelopeList<T>>() {}.type
    return createGson(datePattern).fromJson<EnvelopeList<T>>(this, type).data
}

@PublishedApi
internal inline fun <reified T> String.toEnvelopeList(datePattern: String): EnvelopeList<T> {
    val type = object : TypeToken<EnvelopeList<T>>() {}.type
    return createGson(datePattern).fromJson(this, type)
}

@PublishedApi
internal inline fun <reified T> String.toModel(datePattern: String): T {
    val type = object : TypeToken<T>() {}.type
    return createGson(datePattern).fromJson(this, type)
}

@PublishedApi
internal inline fun <reified T> String.toModelWrapped(datePattern: String): T? {
    val type = object : TypeToken<Envelope<T>>() {}.type
    return createGson(datePattern).fromJson<Envelope<T>>(this, type).data
}

internal fun <T> T.toJson(datePattern: String): String {
    val type = object : TypeToken<T>() {}.type
    return createGson(datePattern).toJson(this, type)
}

@PublishedApi
internal fun createGson(datePattern: String): Gson = GsonBuilder()
    .setDateFormat(datePattern)
    .create()
