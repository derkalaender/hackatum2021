package util

import kotlinx.serialization.json.Json

object Json {
    val jsonTranscoder = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        prettyPrint = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
    }
}