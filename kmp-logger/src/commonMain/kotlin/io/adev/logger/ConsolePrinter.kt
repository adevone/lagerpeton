package io.adev.logger

object ConsolePrinter : Logger.Printer {

    override fun printLog(
        level: Logger.Level,
        owner: String?,
        message: String,
        values: List<Logger.Entry>
    ) {
        val logMessage = Formatter.format(owner, message, values)
        println(logMessage)
    }

    object Formatter {
        fun format(owner: String?, message: String?, values: List<Logger.Entry>): String {
            return buildString {
                owner?.let { owner ->
                    append(owner)
                    append(": ")
                }
                message?.let { message ->
                    append(message)
                }
                values.forEach { entry ->
                    append(", ") // append always because "," is also needed after [message]
                    append(entry.key)
                    append("=")
                    append(entry.stringValue())
                }
            }
        }
    }
}