package io.adev.lagerpeton

class TypedLager<TAccumulator> private constructor(
    private val collector: Collector<TAccumulator>,
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
        log(INFO_LEVEL, message, owner, append)
    }

    fun error(
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        log(ERROR_LEVEL, message, owner, append)
    }

    fun debug(
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        log(DEBUG_LEVEL, message, owner, append)
    }

    fun warning(
        message: String,
        owner: String? = this.owner,
        append: AppendToAccumulator<TAccumulator>? = null
    ) {
        log(WARNING_LEVEL, message, owner, append)
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
            collector.printLog(level, owner, message, accumulator)
        }
    }

    fun copy(
        owner: String? = null,
        onEachLog: AppendToAccumulator<TAccumulator>? = null,
        appendToStored: AppendToAccumulator<TAccumulator>? = null
    ): TypedLager<TAccumulator> {
        return TypedLager(
            collector,
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

    fun interface Collector<TAccumulator> {
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
            collector: Collector<TAccumulator>,
            accumulatorFactory: AccumulatorFactory<TAccumulator>,
            printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
            owner: String? = null,
            onEachLog: AppendToAccumulator<TAccumulator>? = null,
            makeStored: AppendToAccumulator<TAccumulator>? = null
        ): TypedLager<TAccumulator> {
            return TypedLager(
                collector,
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
        const val INFO_LEVEL    = 0b0001
        const val ERROR_LEVEL   = 0b0010
        const val DEBUG_LEVEL   = 0b0100
        const val WARNING_LEVEL = 0b1000
        // @formatter:on

        fun makePrintMask(vararg levels: Int): Int {
            var totalMask = 0
            levels.forEach { level ->
                totalMask = totalMask or level
            }
            return totalMask
        }

        val allLevels: Array<Int>
            get() = arrayOf(
                INFO_LEVEL,
                ERROR_LEVEL,
                DEBUG_LEVEL,
                WARNING_LEVEL
            )
    }
}

typealias AppendToAccumulator<TAccumulator> = (TAccumulator) -> Unit