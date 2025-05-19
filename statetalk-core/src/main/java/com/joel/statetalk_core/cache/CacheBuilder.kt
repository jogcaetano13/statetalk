package com.joel.statetalk_core.cache

import com.joel.statetalk_core.annotations.StateTalkMarker
import com.joel.statetalk_core.exceptions.StateTalkException
import okhttp3.Cache
import java.io.File

@StateTalkMarker
class CacheBuilder internal constructor() {
    var maxSize: CacheSize = 50.megabytes

    var cacheDir: String = ""

    var fileName: String = "statetalk_cache_${System.currentTimeMillis()}"

    var file: File? = null

    internal fun build(): Cache {
        val file = this.file ?: run {
            if (cacheDir.isBlank())
                throw StateTalkException("Cache dir must not be empty!")
            File(cacheDir, fileName)
        }

        return Cache(file, maxSize.size)
    }
}