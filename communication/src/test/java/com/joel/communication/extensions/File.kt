package com.joel.communication.helpers

import com.joel.communication.BaseTest
import java.io.File

private fun String.asResourceString(baseTest: BaseTest): String {
    val file = File(baseTest.javaClass.classLoader?.getResource(this)?.file ?: "")
    return file.readText()
}

fun BaseTest.successResponse(): String = "successResponse.json".asResourceString(this)

fun BaseTest.errorResponse(): String = "errorResponse.json".asResourceString(this)