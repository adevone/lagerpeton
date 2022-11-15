package io.adev.lagerpeton

import com.google.firebase.crashlytics.internal.common.CrashlyticsCore

class FirebaseCollector(
    private val crashlyticsCore: CrashlyticsCore,
) : TypedLager.Collector<PrimitivesOnlyAccumulator> {

    override fun printLog(
        level: Int,
        owner: String?,
        message: String,
        throwable: Throwable?,
        accumulator: PrimitivesOnlyAccumulator
    ) {
        val logMessage = buildString {
            append(message)
            val hasValues = accumulator.values.isNotEmpty()
            if (hasValues) {
                append(", ")
            }
            ConsoleCollector.Formatter.appendParameters(accumulator, builder = this)
        }
        crashlyticsCore.log(logMessage)
        if (throwable != null) {
            crashlyticsCore.logException(throwable)
        }
    }
}

fun TypedLager.Companion.firebase(
    crashlyticsCore: CrashlyticsCore,
    printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
    owner: String? = null,
    onEachLog: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null,
    makeStored: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null
): TypedLager<PrimitivesOnlyAccumulator> {
    return create(
        collector = FirebaseCollector(crashlyticsCore),
        accumulatorFactory = PrimitivesOnlyAccumulator,
        printMask,
        owner,
        onEachLog,
        makeStored
    )
}