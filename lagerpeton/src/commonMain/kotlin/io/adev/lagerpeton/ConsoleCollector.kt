package io.adev.lagerpeton

object ConsoleCollector : TypedLager.Collector<PrimitivesOnlyAccumulator> {

    override fun printLog(
        level: Int,
        owner: String?,
        message: String,
        accumulator: PrimitivesOnlyAccumulator
    ) {
        val logMessage = Formatter.format(owner, message, accumulator)
        println(logMessage)
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

fun TypedLager.Companion.console(
    printMask: Int = INFO or ERROR or DEBUG or WARNING,
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