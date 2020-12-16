package io.adev.lagerpeton

import android.util.Log

object AndroidCollector : TypedLager.Collector<PrimitivesOnlyAccumulator> {

    override fun printLog(
        level: Int,
        owner: String?,
        message: String,
        accumulator: PrimitivesOnlyAccumulator
    ) {
        val logMessage = buildString {
            append(message)
            if (accumulator.values.isNotEmpty()) {
                append(", ")
            }
            ConsoleCollector.Formatter.appendParameters(accumulator, builder = this)
        }
        when (level) {
            Lager.INFO -> {
                Log.i(owner ?: DEFAULT_OWNER, logMessage)
            }
            Lager.ERROR -> {
                Log.e(owner ?: DEFAULT_OWNER, logMessage)
            }
            Lager.DEBUG -> {
                Log.d(owner ?: DEFAULT_OWNER, logMessage)
            }
            Lager.WARNING -> {
                Log.w(owner ?: DEFAULT_OWNER, logMessage)
            }
        }
    }

    private const val DEFAULT_OWNER = "AndroidPrinter"
}

fun TypedLager.Companion.android(
    printMask: Int = INFO or ERROR or DEBUG or WARNING,
    owner: String? = null,
    onEachLog: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null,
    makeStored: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null
): TypedLager<PrimitivesOnlyAccumulator> {
    return create(
        collector = AndroidCollector,
        accumulatorFactory = PrimitivesOnlyAccumulator,
        printMask,
        owner,
        onEachLog,
        makeStored
    )
}