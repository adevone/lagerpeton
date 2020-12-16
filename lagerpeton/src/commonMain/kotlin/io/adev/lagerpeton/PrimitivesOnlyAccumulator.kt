package io.adev.lagerpeton

class PrimitivesOnlyAccumulator private constructor() {
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

    companion object : TypedLager.AccumulatorFactory<PrimitivesOnlyAccumulator> {
        override fun createAccumulator(from: PrimitivesOnlyAccumulator?): PrimitivesOnlyAccumulator {
            val accumulator = PrimitivesOnlyAccumulator()
            if (from != null) {
                accumulator.values.putAll(from.values)
            }
            return accumulator
        }
    }
}