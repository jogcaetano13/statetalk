package com.joel.jlibtemplate.room.converters

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromDate(date: Date?): String? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return date?.let {
            format.format(it)
        }
    }

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateString?.let {
            dateFormat.parse(it)
        }
    }
}