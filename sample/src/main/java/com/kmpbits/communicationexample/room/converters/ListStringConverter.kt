package com.kmpbits.communicationexample.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ListStringConverter {

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        if (value == null)
            return Collections.emptyList()

        return Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromList(items: List<String>?): String? = Gson().toJson(items)
}