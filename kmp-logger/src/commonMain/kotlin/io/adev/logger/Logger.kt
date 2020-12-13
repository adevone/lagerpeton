package io.adev.logger

class Logger<TAccumulator> @PublishedApi internal constructor(
    @PublishedApi internal val printer: Printer<TAccumulator>,
    @PublishedApi internal val printMask: Int,
    @PublishedApi internal val owner: String?,
    @PublishedApi internal val appends: Array<LoggerAppend<TAccumulator>>
) {
    fun info(message: String, append: LoggerAppend<TAccumulator> = {}) {
        if (printMask and I_MASK != 0) {
            val accumulator = currentAccumulator()
            append(accumulator)
            printer.printLog(Level.Info, owner, message, accumulator)
        }
    }

    fun error(message: String, append: LoggerAppend<TAccumulator> = {}) {
        if (printMask and E_MASK != 0) {
            val accumulator = currentAccumulator()
            append(accumulator)
            printer.printLog(Level.Error, owner, message, accumulator)
        }
    }

    fun debug(message: String, append: LoggerAppend<TAccumulator> = {}) {
        if (printMask and D_MASK != 0) {
            val accumulator = currentAccumulator()
            append(accumulator)
            printer.printLog(Level.Debug, owner, message, accumulator)
        }
    }

    fun warning(message: String, append: LoggerAppend<TAccumulator> = {}) {
        if (printMask and W_MASK != 0) {
            val accumulator = currentAccumulator()
            append(accumulator)
            printer.printLog(Level.Warning, owner, message, accumulator)
        }
    }

    private fun currentAccumulator(): TAccumulator {
        val accumulator = printer.createAccumulator()
        appends.forEach { append ->
            append(accumulator)
        }
        return accumulator
    }

    fun new(owner: String? = null, append: LoggerAppend<TAccumulator>): Logger<TAccumulator> {
        return Logger(
            printer = printer,
            printMask = printMask,
            owner = owner ?: this.owner,
            appends = appends + append
        )
    }

    interface Printer<TAccumulator> {
        fun createAccumulator(): TAccumulator
        fun printLog(level: Level, owner: String?, message: String, accumulator: TAccumulator)
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
        fun <TAccumulator> new(
            printer: Printer<TAccumulator>,
            printMask: Int = ALL_MASK,
            owner: String? = null,
            append: LoggerAppend<TAccumulator> = {}
        ): Logger<TAccumulator> {
            return Logger(printer, printMask, owner, arrayOf(append))
        }
    }
}

typealias LoggerAppend<TAccumulator> = (TAccumulator) -> Unit