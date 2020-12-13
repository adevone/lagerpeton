package io.adev.logger

import android.util.Log

object AndroidPrinter : Logger.Printer<PrimitivesOnlyAccumulator> {

    override fun printLog(
        level: Logger.Level,
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
            Logger.Level.Info -> {
                Log.i(owner ?: DEFAULT_OWNER, logMessage)
            }
            Logger.Level.Error -> {
                Log.e(owner ?: DEFAULT_OWNER, logMessage)
            }
            Logger.Level.Debug -> {
                Log.d(owner ?: DEFAULT_OWNER, logMessage)
            }
            Logger.Level.Warning -> {
                Log.w(owner ?: DEFAULT_OWNER, logMessage)
            }
        }
    }

    override fun createAccumulator(): PrimitivesOnlyAccumulator {
        return PrimitivesOnlyAccumulator()
    }

    private const val DEFAULT_OWNER = "AndroidPrinter"
}