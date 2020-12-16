package io.adev.lagerpeton

import android.util.Log

object AndroidPrinter : TypedLager.Printer<PrimitivesOnlyAccumulator> {

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
            ConsolePrinter.Formatter.appendParameters(accumulator, builder = this)
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
        AndroidPrinter,
        PrimitivesOnlyAccumulator,
        printMask,
        owner,
        onEachLog,
        makeStored
    )
}