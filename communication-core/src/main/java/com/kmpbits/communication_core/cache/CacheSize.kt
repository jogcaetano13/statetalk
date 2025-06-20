package com.kmpbits.communication_core.cache

@JvmInline
value class CacheSize(val size: Long)

val Int.kilobytes: CacheSize
    get() {
        val kb = this * 1024
        return CacheSize(kb.toLong())
    }

val Int.megabytes: CacheSize
    get() {
        val mb = this * 1024 * 1024
        return CacheSize(mb.toLong())
    }