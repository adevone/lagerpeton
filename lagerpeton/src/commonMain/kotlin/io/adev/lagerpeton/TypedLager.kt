package io.adev.lagerpeton

class TypedLager<TAccumulator> private constructor(
    private val printer: Printer<TAccumulator>,
    private val printMask: Int,
    private val onEachLogAppends: Array<AppendToAccumulator<TAccumulator>>?,
    private val owner: String?
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
            val accumulator = printer.createAccumulator()
            onEachLogAppends?.forEach { thisAppend ->
                thisAppend(accumulator)
            }
            append(accumulator)
            printer.printLog(level, owner, message, accumulator)
        }
    }

    fun new(
        onEachLog: AppendToAccumulator<TAccumulator>?,
        owner: String? = null
    ): TypedLager<TAccumulator> {
        return TypedLager(
            printer = printer,
            printMask = printMask,
            owner = owner ?: this.owner,
            onEachLogAppends = if (onEachLog != null) {
                (this.onEachLogAppends ?: emptyArray()) + onEachLog
            } else {
                this.onEachLogAppends
            }
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
            onEachLog: AppendToAccumulator<TAccumulator>? = null,
            owner: String? = null
        ): TypedLager<TAccumulator> {
            return TypedLager(
                printer,
                printMask,
                onEachLogAppends = onEachLog?.let { arrayOf(it) },
                owner
            )
        }

        // @formatter:off
        const val INFO    = 0b0001
        const val ERROR   = 0b0010
        const val DEBUG   = 0b0100
        const val WARNING = 0b1000
        // @formatter:on

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