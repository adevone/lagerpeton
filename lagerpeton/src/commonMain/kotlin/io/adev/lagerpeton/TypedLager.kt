package io.adev.lagerpeton

class TypedLager<TAccumulator> private constructor(
    private val printer: Printer<TAccumulator>,
    private val accumulatorFactory: AccumulatorFactory<TAccumulator>,
    private val printMask: Int,
    private val owner: String?,
    private val onEachLogAppends: Array<AppendToAccumulator<TAccumulator>>?,
    private val storedAccumulator: TAccumulator?
) {
    fun info(
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        log(INFO, message, owner, append)
    }

    fun error(
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        log(ERROR, message, owner, append)
    }

    fun debug(
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        log(DEBUG, message, owner, append)
    }

    fun warning(
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        log(WARNING, message, owner, append)
    }

    fun log(
        level: Int,
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        if (printMask and level != 0) {
            val accumulator = accumulatorFactory.createAccumulator(from = storedAccumulator)
            onEachLogAppends?.forEach { thisAppend ->
                thisAppend(accumulator)
            }
            if (append != null) {
                append(accumulator)
            }
            printer.printLog(level, owner, message, accumulator)
        }
    }

    fun copy(
        owner: String? = null,
        onEachLog: AppendToAccumulator<TAccumulator>? = null,
        appendToStored: AppendToAccumulator<TAccumulator>? = null
    ): TypedLager<TAccumulator> {
        return TypedLager(
            printer,
            accumulatorFactory,
            printMask,
            owner = owner ?: this.owner,
            onEachLogAppends = if (onEachLog != null) {
                (this.onEachLogAppends ?: emptyArray()) + onEachLog
            } else {
                this.onEachLogAppends
            },
            storedAccumulator = if (appendToStored != null) {
                val accumulator = accumulatorFactory.createAccumulator(from = storedAccumulator)
                appendToStored(accumulator)
                accumulator
            } else {
                storedAccumulator
            }
        )
    }

    fun interface Printer<TAccumulator> {
        fun printLog(level: Int, owner: String?, message: String, accumulator: TAccumulator)
    }

    interface AccumulatorFactory<TAccumulator> {
        fun createAccumulator(from: TAccumulator?): TAccumulator
    }

    companion object {

        /**
         * [printMask] can be created by [makePrintMask]
         */
        fun <TAccumulator> create(
            printer: Printer<TAccumulator>,
            accumulatorFactory: AccumulatorFactory<TAccumulator>,
            printMask: Int = INFO or ERROR or DEBUG or WARNING,
            owner: String? = null,
            onEachLog: AppendToAccumulator<TAccumulator>? = null,
            makeStored: AppendToAccumulator<TAccumulator>? = null
        ): TypedLager<TAccumulator> {
            return TypedLager(
                printer,
                accumulatorFactory,
                printMask,
                owner,
                onEachLogAppends = onEachLog?.let { arrayOf(it) },
                storedAccumulator = if (makeStored != null) {
                    val accumulator = accumulatorFactory.createAccumulator(from = null)
                    makeStored(accumulator)
                    accumulator
                } else {
                    null
                }
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