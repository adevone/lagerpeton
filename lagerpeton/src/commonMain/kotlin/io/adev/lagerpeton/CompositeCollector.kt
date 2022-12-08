package io.adev.lagerpeton

import io.adev.lagerpeton.TypedLager.Companion.DEBUG_LEVEL
import io.adev.lagerpeton.TypedLager.Companion.ERROR_LEVEL
import io.adev.lagerpeton.TypedLager.Companion.INFO_LEVEL
import io.adev.lagerpeton.TypedLager.Companion.WARNING_LEVEL

class CompositeCollector<TAccumulator>(
    private val collectors: List<ComposedCollector<TAccumulator>>,
) : TypedLager.Collector<TAccumulator>() {

    override fun printLog(
        level: Int,
        owner: String?,
        message: String,
        throwable: Throwable?,
        accumulator: TAccumulator
    ) {
        collectors.forEach { (collector, printMask) ->
            if (level and printMask != 0) {
                collector.printLog(level, owner, message, throwable, accumulator)
            }
        }
    }
}

data class ComposedCollector<TAccumulator>(
    val collector: TypedLager.Collector<TAccumulator>,
    val printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
)

fun TypedLager.Companion.composite(
    collectors: List<ComposedCollector<PrimitivesOnlyAccumulator>>,
    printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
    owner: String? = null,
    onEachLog: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null,
    makeStored: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null
): TypedLager<PrimitivesOnlyAccumulator> {
    return create(
        collector = CompositeCollector(collectors),
        accumulatorFactory = PrimitivesOnlyAccumulator,
        printMask,
        owner,
        onEachLog,
        makeStored
    )
}