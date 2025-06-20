package com.kmpbits.communication_core.request

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.File


internal class UploadFileRequestBody(
    private val file: File,
    private val contentType: String,
    private val onProgress: (Long) -> Unit
) : RequestBody() {

    companion object {
        private const val SEGMENT_SIZE = 2048 // okio.Segment.SIZE
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun contentType(): MediaType? {
        return contentType.toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        var source: Source? = null
        try {
            source = file.source()
            var total: Long = 0
            var read: Long
            while (source.read(sink.buffer, SEGMENT_SIZE.toLong()).also { read = it } != -1L) {
                total += read
                sink.flush()
                onProgress(total)
            }
        } finally {
            source?.close()
        }
    }
}