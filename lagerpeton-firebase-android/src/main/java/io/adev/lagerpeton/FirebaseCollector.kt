package io.adev.lagerpeton

import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCollector(
    private val crashlytics: FirebaseCrashlytics,
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
        crashlytics.log(logMessage)
        if (throwable != null) {
            crashlytics.recordException(throwable)
        }
    }
}

fun TypedLager.Companion.firebase(
    crashlytics: FirebaseCrashlytics,
    printMask: Int = INFO_LEVEL or ERROR_LEVEL or DEBUG_LEVEL or WARNING_LEVEL,
    owner: String? = null,
    onEachLog: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null,
    makeStored: AppendToAccumulator<PrimitivesOnlyAccumulator>? = null
): TypedLager<PrimitivesOnlyAccumulator> {
    return create(
        collector = FirebaseCollector(crashlytics),
        accumulatorFactory = PrimitivesOnlyAccumulator,
        printMask,
        owner,
        onEachLog,
        makeStored
    )
}