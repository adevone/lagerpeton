package io.adev.lagerpeton

class PrimitivesOnlyAccumulator {
    val values: MutableMap<String, Any> = mutableMapOf()

    fun put(key: String, value: String) {
        values[key] = value
    }

    fun put(key: String, value: Int) {
        values[key] = value
    }

    fun put(key: String, value: Long) {
        values[key] = value
    }

    fun put(key: String, value: Float) {
        values[key] = value
    }

    fun put(key: String, value: Double) {
        values[key] = value
    }

    fun put(key: String, value: Boolean) {
        values[key] = value
    }
}