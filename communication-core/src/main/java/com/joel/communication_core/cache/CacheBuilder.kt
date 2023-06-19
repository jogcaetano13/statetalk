package com.joel.communication_core.cache

import com.joel.communication_core.annotations.CommunicationsMarker
import com.joel.communication_core.exceptions.CommunicationsException
import okhttp3.Cache
import java.io.File

@CommunicationsMarker
class CacheBuilder internal constructor() {
    var maxSize: CacheSize = 50.megabytes

    var cacheDir: String = ""

    var fileName: String = "communication_cache_${System.currentTimeMillis()}"

    var file: File? = null

    internal fun build(): Cache {
        val file = this.file ?: run {
            if (cacheDir.isBlank())
                throw CommunicationsException("Cache dir must not be empty!")
            File(cacheDir, fileName)
        }

        return Cache(file, maxSize.size)
    }
}