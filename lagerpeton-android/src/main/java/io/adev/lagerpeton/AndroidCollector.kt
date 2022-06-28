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
            Lager.INFO_LEVEL -> {
                Log.i(owner ?: DEFAULT_OWNER, logMessage)
            }
            Lager.ERROR_LEVEL -> {
                Log.e(owner ?: DEFAULT_OWNER, logMessage)
            }
            Lager.DEBUG_LEVEL -> {
                Log.d(owner ?: DEFAULT_OWNER, logMessage)
            }
            Lager.WARNING_LEVEL -> {
                Log.w(owner ?: DEFAULT_OWNER, logMessage)
            }
        }
    }

    private const val DEFAULT_OWNER = "AndroidPrinter"
}

fun TypedLager.Companion.android(
    printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
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