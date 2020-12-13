package io.adev.logger

import kotlin.reflect.KClass

class Logger private constructor(
    @PublishedApi internal val owner: String?,
    @PublishedApi internal val values: List<Entry>,
    @PublishedApi internal val printer: Printer,
    @PublishedApi internal val printMask: Int
) {
    fun info(message: String, append: (Appender) -> Unit = {}) {
        if (printMask and I_MASK != 0) {
            val appender = currentAppender()
            append(appender)
            printer.printLog(Level.Info, appender.owner, message, appender.values)
        }
    }

    fun error(message: String, append: (Appender) -> Unit = {}) {
        if (printMask and E_MASK != 0) {
            val appender = currentAppender()
            append(appender)
            printer.printLog(Level.Error, appender.owner, message, appender.values)
        }
    }

    fun debug(message: String, append: (Appender) -> Unit = {}) {
        if (printMask and D_MASK != 0) {
            val appender = currentAppender()
            append(appender)
            printer.printLog(Level.Debug, appender.owner, message, appender.values)
        }
    }

    fun warning(message: String, append: (Appender) -> Unit = {}) {
        if (printMask and W_MASK != 0) {
            val appender = currentAppender()
            append(appender)
            printer.printLog(Level.Warning, appender.owner, message, appender.values)
        }
    }

    private fun currentAppender(): Appender {
        return Appender(printMask, owner, values.toMutableList())
    }

    /**
     * Create new [Logger] that initially contains [values] of this logger
     */
    inline fun new(append: (Appender) -> Unit): Logger {
        val appender = Appender(printMask, owner, values.toMutableList())
        append(appender)
        return appender.append(printer)
    }

    class Appender(
        private val printMask: Int,
        internal var owner: String?,
        internal val values: MutableList<Entry>
    ) {
        fun own(owner: String) {
            this.owner = owner
        }

        fun own(owner: KClass<*>) {
            owner.simpleName?.let { simpleName ->
                this.owner = simpleName
            }
        }

        fun put(key: String, value: String) {
            put(Entry.Str(key, value))
        }

        fun put(key: String, value: Int) {
            put(Entry.Integer(key, value))
        }

        fun put(key: String, value: Long) {
            put(Entry.Lng(key, value))
        }

        fun put(key: String, value: Float) {
            put(Entry.Flt(key, value))
        }

        fun put(key: String, value: Double) {
            put(Entry.Dbl(key, value))
        }

        fun put(key: String, value: Boolean) {
            put(Entry.Booln(key, value))
        }

        fun put(entry: Entry) {
            values.removeAll { it.key == entry.key }
            values.add(entry)
        }

        fun append(printer: Printer) = Logger(owner, values, printer, printMask)
    }

    abstract class Entry {
        abstract val key: String
        abstract fun stringValue(): String

        data class Integer(
            override val key: String,
            val value: Int
        ) : Entry() {
            override fun stringValue(): String = value.toString()
        }

        data class Lng(
            override val key: String,
            val value: Long
        ) : Entry() {
            override fun stringValue(): String = value.toString()
        }

        data class Flt(
            override val key: String,
            val value: Float
        ) : Entry() {
            override fun stringValue(): String = value.toString()
        }

        data class Dbl(
            override val key: String,
            val value: Double
        ) : Entry() {
            override fun stringValue(): String = value.toString()
        }

        data class Booln(
            override val key: String,
            val value: Boolean
        ) : Entry() {
            override fun stringValue(): String = value.toString()
        }

        data class Str(
            override val key: String,
            val value: String
        ) : Entry() {
            override fun stringValue(): String = value
        }
    }

    interface Printer {
        fun printLog(level: Level, owner: String?, message: String, values: List<Entry>)
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
        const val ALL_MASK = I_MASK or E_MASK or D_MASK or W_MASK

        /**
         * [printMask] can be created by [Level.printMask]
         */
        inline fun new(
            printer: Printer,
            printMask: Int = ALL_MASK,
            owner: String? = null,
            append: (Appender) -> Unit = {}
        ): Logger {
            val appender = Appender(printMask, owner, mutableListOf())
            append(appender)
            return appender.append(printer)
        }
    }
}