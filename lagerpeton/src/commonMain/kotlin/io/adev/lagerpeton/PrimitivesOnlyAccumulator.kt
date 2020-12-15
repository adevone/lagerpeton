package io.adev.lagerpeton

class PrimitivesOnlyAccumulator(from: PrimitivesOnlyAccumulator?) {
    val values: MutableMap<String, Any> = mutableMapOf()

    init {
        if (from != null) {
            values.putAll(from.values)
        }
    }

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