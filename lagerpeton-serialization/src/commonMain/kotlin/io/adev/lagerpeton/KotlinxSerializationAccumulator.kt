package io.adev.lagerpeton

import kotlinx.serialization.json.JsonArrayBuilder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

class KotlinxSerializationAccumulator {
    val content: MutableMap<String, JsonElement> = linkedMapOf()

    fun putJsonObject(key: String, builderAction: JsonObjectBuilder.() -> Unit): JsonElement? {
        return put(key, buildJsonObject(builderAction))
    }

    fun putJsonArray(key: String, builderAction: JsonArrayBuilder.() -> Unit): JsonElement? {
        return put(key, buildJsonArray(builderAction))
    }

    fun put(key: String, value: Boolean?): JsonElement? {
        return put(key, JsonPrimitive(value))
    }

    fun put(key: String, value: Number?): JsonElement? {
        return put(key, JsonPrimitive(value))
    }

    fun put(key: String, value: String?): JsonElement? {
        return put(key, JsonPrimitive(value))
    }

    fun put(key: String, element: JsonElement): JsonElement? {
        return content.put(key, element)
    }

    fun buildJsonObject(): JsonObject = JsonObject(content)

    companion object : TypedLager.AccumulatorFactory<KotlinxSerializationAccumulator> {
        override fun createAccumulator(
            from: KotlinxSerializationAccumulator?
        ): KotlinxSerializationAccumulator {
            val accumulator = KotlinxSerializationAccumulator()
            if (from != null) {
                accumulator.content.putAll(from.content)
            }
            return KotlinxSerializationAccumulator()
        }
    }
}

fun TypedLager.Companion.kotlinxSerialization(
    collect: (json: JsonObject) -> Unit,
    collector: TypedLager.Collector<KotlinxSerializationAccumulator> =
        object : TypedLager.Collector<KotlinxSerializationAccumulator>() {
            override fun printLog(
                level: Int,
                owner: String?,
                message: String,
                throwable: Throwable?,
                accumulator: KotlinxSerializationAccumulator
            ) {
                val jsonLevel: String? = when (level) {
                    Lager.INFO_LEVEL -> "info"
                    Lager.ERROR_LEVEL -> "error"
                    Lager.DEBUG_LEVEL -> "debug"
                    Lager.WARNING_LEVEL -> "warning"
                    else -> null
                }
                if (jsonLevel != null) {
                    accumulator.put("level", jsonLevel)
                }
                accumulator.put("from", owner)
                accumulator.put("message", message)
                if (throwable != null) {
                    accumulator.put("stacktrace", throwable.stackTraceToString())
                }
                val json = accumulator.buildJsonObject()
                collect(json)
            }
        },
    printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
    owner: String? = null,
    onEachLog: AppendToAccumulator<KotlinxSerializationAccumulator>? = null,
    makeStored: AppendToAccumulator<KotlinxSerializationAccumulator>? = null
): TypedLager<KotlinxSerializationAccumulator> {
    return create(
        collector,
        accumulatorFactory = KotlinxSerializationAccumulator,
        printMask,
        owner,
        onEachLog,
        makeStored
    )
}