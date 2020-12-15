package io.adev.lagerpeton

object ConsolePrinter : TypedLager.Printer<PrimitivesOnlyAccumulator> {

    override fun printLog(
        level: Int,
        owner: String?,
        message: String,
        accumulator: PrimitivesOnlyAccumulator
    ) {
        val logMessage = Formatter.format(owner, message, accumulator)
        println(logMessage)
    }

    override fun createAccumulator(from: PrimitivesOnlyAccumulator?): PrimitivesOnlyAccumulator {
        return PrimitivesOnlyAccumulator(from)
    }

    object Formatter {

        fun format(
            owner: String?,
            message: String?,
            accumulator: PrimitivesOnlyAccumulator
        ): String {
            return buildString {
                owner?.let { owner ->
                    append(owner)
                    append(": ")
                }
                message?.let { message ->
                    append(message)
                }
                if (accumulator.values.isNotEmpty()) {
                    append(", ")
                }
                appendParameters(accumulator, builder = this)
            }
        }

        fun appendParameters(
            accumulator: PrimitivesOnlyAccumulator,
            builder: StringBuilder
        ) {
            builder.apply {
                var isFirstParameter = true
                accumulator.values.entries.reversed().forEach { entry ->
                    if (isFirstParameter) {
                        isFirstParameter = false
                    } else {
                        append(", ")
                    }
                    append(entry.key)
                    append("=")
                    append(entry.value)
                }
            }
        }
    }
}