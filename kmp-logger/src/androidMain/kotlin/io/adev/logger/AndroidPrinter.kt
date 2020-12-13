package io.adev.logger

import android.util.Log

object AndroidPrinter : Logger.Printer<PrimitivesOnlyAccumulator> {

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
            Logger.INFO -> {
                Log.i(owner ?: DEFAULT_OWNER, logMessage)
            }
            Logger.ERROR -> {
                Log.e(owner ?: DEFAULT_OWNER, logMessage)
            }
            Logger.DEBUG -> {
                Log.d(owner ?: DEFAULT_OWNER, logMessage)
            }
            Logger.WARNING -> {
                Log.w(owner ?: DEFAULT_OWNER, logMessage)
            }
        }
    }

    override fun createAccumulator(): PrimitivesOnlyAccumulator {
        return PrimitivesOnlyAccumulator()
    }

    private const val DEFAULT_OWNER = "AndroidPrinter"
}