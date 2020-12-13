package io.adev.logger

object ConsolePrinter : Logger.Printer {

    override fun printLog(
        level: Logger.Level,
        owner: String?,
        message: String,
        values: MutableMap<String, Any?>
    ) {
        val logMessage = Formatter.format(owner, message, values)
        println(logMessage)
    }

    object Formatter {
        fun format(owner: String?, message: String?, values: Map<String, Any?>): String {
            return buildString {
                owner?.let { owner ->
                    append(owner)
                    append(": ")
                }
                message?.let { message ->
                    append(message)
                }
                values.onEach { entry ->
                    append(", ") // append always because "," is also needed after [message]
                    append(entry.key)
                    append("=")
                    append(entry.value)
                }
            }
        }
    }
}