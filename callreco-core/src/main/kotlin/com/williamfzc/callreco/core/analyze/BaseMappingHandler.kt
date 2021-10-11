package com.williamfzc.callreco.core.analyze

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

open class BaseMappingHandler {
    var methodMapping = mutableMapOf<Long, String>()

    fun dump(): String {
        return Json.encodeToString(this.methodMapping)
    }

    fun load(jsonContent: String) {
        this.methodMapping = Json.decodeFromString(jsonContent)
    }

    fun load(jsonFile: File) = load(jsonFile.readText())

    fun isEmpty(): Boolean {
        return methodMapping.isEmpty()
    }
}