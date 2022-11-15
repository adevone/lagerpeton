package io.adev.lagerpeton

object ConsoleCollector : TypedLager.Collector<PrimitivesOnlyAccumulator> {

    override fun printLog(
        level: Int,
        owner: String?,
        message: String,
        throwable: Throwable?,
        accumulator: PrimitivesOnlyAccumulator
    ) {
        val logMessage = Formatter.format(owner, message, throwable, accumulator)
        println(logMessage)
    }

    object Formatter {

        fun format(
            owner: String?,
            message: String?,
            throwable: Throwable?,
            accumulator: PrimitivesOnlyAccumulator
        ): String {
            return buildString {
                if (owner != null) {
                    append(owner)
                    append(": ")
                }
                if (message != null) {
                    append(message)
                }
                val hasValues = accumulator.values.isNotEmpty()
                if (hasValues) {
                    append(", ")
                }
                appendParameters(accumulator, builder = this)
                if (throwable != null) {
                    if (message?.isNotEmpty() == true || hasValues) {
                        append(", ")
                    }
                    append("stacktrace=")
                    append(throwable.stackTraceToString())
                }
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

fun TypedLager.Companion.console(
    printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
    owner: String? = null,
    onEachLog: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null,
    makeStored: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null
): TypedLager<PrimitivesOnlyAccumulator> {
    return create(
        collector = ConsoleCollector,
        accumulatorFactory = PrimitivesOnlyAccumulator,
        printMask,
        owner,
        onEachLog,
        makeStored
    )
}