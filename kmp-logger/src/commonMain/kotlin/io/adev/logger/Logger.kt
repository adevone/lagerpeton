package io.adev.logger

class Logger @PublishedApi internal constructor(
    @PublishedApi internal val printer: Printer,
    @PublishedApi internal val printMask: Int,
    @PublishedApi internal val owner: String?,
    @PublishedApi internal val appends: Array<LoggerAppend>
) {
    fun info(message: String, append: LoggerAppend = {}) {
        if (printMask and I_MASK != 0) {
            val builder = currentBuilder()
            append(builder)
            printer.printLog(Level.Info, owner, message, builder.values)
        }
    }

    fun error(message: String, append: LoggerAppend = {}) {
        if (printMask and E_MASK != 0) {
            val builder = currentBuilder()
            append(builder)
            printer.printLog(Level.Error, owner, message, builder.values)
        }
    }

    fun debug(message: String, append: LoggerAppend = {}) {
        if (printMask and D_MASK != 0) {
            val builder = currentBuilder()
            append(builder)
            printer.printLog(Level.Debug, owner, message, builder.values)
        }
    }

    fun warning(message: String, append: LoggerAppend = {}) {
        if (printMask and W_MASK != 0) {
            val builder = currentBuilder()
            append(builder)
            printer.printLog(Level.Warning, owner, message, builder.values)
        }
    }

    private fun currentBuilder(): Builder {
        val builder = Builder()
        appends.forEach { append ->
            append(builder)
        }
        return builder
    }

    fun new(owner: String? = null, append: LoggerAppend): Logger {
        return Logger(
            printer = printer,
            printMask = printMask,
            owner = owner ?: this.owner,
            appends = appends + append
        )
    }

    class Builder {
        internal val values: MutableMap<String, Any?> = mutableMapOf()

        fun put(key: String, value: String) {
            values[key] = value
        }

        fun put(key: String, value: Int) {
            values[key] = value
        }

        fun put(key: String, value: Long) {
            values[key] = value
        }

        fun put(key: String, value: Float) {
            values[key] = value
        }

        fun put(key: String, value: Double) {
            values[key] = value
        }

        fun put(key: String, value: Boolean) {
            values[key] = value
        }

        /**
         * Don't forget to handle this type of value in your [Printer]
         */
        fun putUnsafe(key: String, value: Any?) {
            values[key] = value
        }
    }

    interface Printer {
        fun printLog(
            level: Level,
            owner: String?,
            message: String,
            values: MutableMap<String, Any?>
        )
    }

    enum class Level {
        Info {
            override val printMask: Int = I_MASK
        },
        Error {
            override val printMask: Int = E_MASK
        },
        Debug {
            override val printMask: Int = D_MASK
        },
        Warning {
            override val printMask: Int = W_MASK
        };

        abstract val printMask: Int

        companion object {
            fun printMask(vararg levels: Level): Int {
                var totalMask = 0
                levels.forEach { level ->
                    totalMask = totalMask or level.printMask
                }
                return totalMask
            }
        }
    }

    companion object {
        private const val I_MASK = 0b0001
        private const val E_MASK = 0b0010
        private const val D_MASK = 0b0100
        private const val W_MASK = 0b1000
        private const val ALL_MASK = I_MASK or E_MASK or D_MASK or W_MASK

        /**
         * [printMask] can be created by [Level.printMask]
         */
        fun new(
            printer: Printer,
            printMask: Int = ALL_MASK,
            owner: String? = null,
            append: LoggerAppend = {}
        ): Logger {
            return Logger(printer, printMask, owner, arrayOf(append))
        }
    }
}

typealias LoggerAppend = (Logger.Builder) -> Unit