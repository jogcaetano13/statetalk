package com.joel.communication.valueclasses

@JvmInline
value class Duration private constructor(val millis: Long) {

    companion object {

        fun millis(millis: Long) = Duration(millis)

        fun seconds(seconds: Long) = Duration(seconds * 1000)

        fun minutes(minutes: Int) = Duration(minutes * 60 * 1000L)

        fun hours(hours: Int) = Duration(hours * 60 * 60 * 1000L)

        fun zero() = Duration(0)
    }
}