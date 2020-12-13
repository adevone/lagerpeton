package io.adev.logger

import android.util.Log

object AndroidPrinter : Logger.Printer {

    override fun printLog(
        level: Logger.Level,
        owner: String?,
        message: String,
        values: MutableMap<String, Any?>
    ) {
        when (level) {
            Logger.Level.Info -> {
                Log.i(owner ?: DEFAULT_OWNER, message ?: "")
            }
            Logger.Level.Error -> {
                Log.e(owner ?: DEFAULT_OWNER, message ?: "")
            }
            Logger.Level.Debug -> {
                Log.d(owner ?: DEFAULT_OWNER, message ?: "")
            }
            Logger.Level.Warning -> {
                Log.w(owner ?: DEFAULT_OWNER, message ?: "")
            }
        }
    }

    private const val DEFAULT_OWNER = "AndroidPrinter"
}