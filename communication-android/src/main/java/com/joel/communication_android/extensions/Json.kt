package com.joel.communication_android.extensions

import org.json.JSONException
import org.json.JSONObject

internal fun String.toJsonObject(): JSONObject? {
    if (isEmpty())
        return null

    return try {
        JSONObject(this)

    } catch (e: JSONException) {
        e.printStackTrace()
        null
    }
}