package io.adev.lagerpeton

class Lager<TAccumulator> private constructor(
    private val printer: Printer<TAccumulator>,
    private val printMask: Int,
    private val owner: String?,
    private val appends: Array<AppendToAccumulator<TAccumulator>>
) {
    fun info(message: String, append: AppendToAccumulator<TAccumulator> = {}) {
        log(INFO, message, append)
    }

    fun error(message: String, append: AppendToAccumulator<TAccumulator> = {}) {
        log(ERROR, message, append)
    }

    fun debug(message: String, append: AppendToAccumulator<TAccumulator> = {}) {
        log(DEBUG, message, append)
    }

    fun warning(message: String, append: AppendToAccumulator<TAccumulator> = {}) {
        log(WARNING, message, append)
    }

    fun log(level: Int, message: String, append: AppendToAccumulator<TAccumulator> = {}) {
        if (printMask and level != 0) {
            val accumulator = currentAccumulator()
            append(accumulator)
            printer.printLog(level, owner, message, accumulator)
        }
    }

    private fun currentAccumulator(): TAccumulator {
        val accumulator = printer.createAccumulator()
        appends.forEach { append ->
            append(accumulator)
        }
        return accumulator
    }

    fun new(owner: String? = null, append: AppendToAccumulator<TAccumulator>): Lager<TAccumulator> {
        return Lager(
            printer = printer,
            printMask = printMask,
            owner = owner ?: this.owner,
            appends = appends + append
        )
    }

    interface Printer<TAccumulator> {
        fun createAccumulator(): TAccumulator
        fun printLog(level: Int, owner: String?, message: String, accumulator: TAccumulator)
    }

    companion object {

        /**
         * [printMask] can be created by [makePrintMask]
         */
        fun <TAccumulator> new(
            printer: Printer<TAccumulator>,
            printMask: Int = INFO or ERROR or DEBUG or WARNING,
            owner: String? = null,
            append: AppendToAccumulator<TAccumulator> = {}
        ): Lager<TAccumulator> {
            return Lager(printer, printMask, owner, arrayOf(append))
        }

        const val INFO = 0b0001
        const val ERROR = 0b0010
        const val DEBUG = 0b0100
        const val WARNING = 0b1000

        fun makePrintMask(vararg levels: Int): Int {
            var totalMask = 0
            levels.forEach { level ->
                totalMask = totalMask or level
            }
            return totalMask
        }

        val allLevels: Array<Int> get() = arrayOf(INFO, ERROR, DEBUG, WARNING)
    }
}

typealias AppendToAccumulator<TAccumulator> = (TAccumulator) -> Unit