package com.joel.communication.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joel.communication.models.ErrorResponse

inline fun <reified T> ErrorResponse.toModel(): T {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson(errorBody, type)
}