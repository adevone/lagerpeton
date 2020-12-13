package io.adev.lagerpeton

import android.util.Log

object AndroidPrinter : Lager.Printer<PrimitivesOnlyAccumulator> {

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

    override fun createAccumulator(): PrimitivesOnlyAccumulator {
        return PrimitivesOnlyAccumulator()
    }

    private const val DEFAULT_OWNER = "AndroidPrinter"
}