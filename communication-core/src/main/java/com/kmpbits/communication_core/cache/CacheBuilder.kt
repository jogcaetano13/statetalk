package com.kmpbits.communication_core.cache

import com.kmpbits.communication_core.annotations.CommunicationMarker
import com.kmpbits.communication_core.exceptions.CommunicationException
import okhttp3.Cache
import java.io.File

@CommunicationMarker
class CacheBuilder internal constructor() {
    var maxSize: CacheSize = 50.megabytes

    var cacheDir: String = ""

    var fileName: String = "statetalk_cache_${System.currentTimeMillis()}"

    var file: File? = null

    internal fun build(): Cache {
        val file = this.file ?: run {
            if (cacheDir.isBlank())
                throw CommunicationException("Cache dir must not be empty!")
            File(cacheDir, fileName)
        }

        return Cache(file, maxSize.size)
    }
}