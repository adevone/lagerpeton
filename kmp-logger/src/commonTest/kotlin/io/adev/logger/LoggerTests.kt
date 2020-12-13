package io.adev.logger

import kotlin.test.Test
import kotlin.test.assertEquals

class LoggerTests {

    @Test
    fun message() {
        val message = "test"
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info(message)
        assertEquals(message, printer.message)
    }

    @Test
    fun printValue() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Str(key, value)), printer.values)
    }

    @Test
    fun globalValue() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Logger.new(printer) {
            it.put(key, value)
        }
        logger.info("")
        assertEquals(listOf(Logger.Entry.Str(key, value)), printer.values)
    }

    @Test
    fun copyValue() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val globalLogger = Logger.new(printer)
        val logger = globalLogger.new {
            it.put(key, value)
        }
        logger.info("")
        assertEquals(listOf(Logger.Entry.Str(key, value)), printer.values)
    }

    @Test
    fun overrideGlobalInCopy() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Logger.new(printer) {
            it.put(key, "notTest")
        }.new {
            it.put(key, value)
        }
        logger.info("")
        assertEquals(listOf(Logger.Entry.Str(key, value)), printer.values)
    }

    @Test
    fun overrideGlobalOnPrint() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Logger.new(printer) {
            it.put(key, "notTest")
        }
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Str(key, value)), printer.values)
    }

    @Test
    fun overrideCopyOnPrint() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Logger.new(printer) {
            it.put(key, "notTest")
        }.new {
            it.put(key, "notTest2")
        }
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Str(key, value)), printer.values)
    }

    @Test
    fun merge() {
        val key1 = "testKey1"
        val value1 = "test1"
        val key2 = "testKey2"
        val value2 = "test2"
        val key3 = "testKey3"
        val value3 = "test3"
        val printer = MockPrinter()
        val logger = Logger.new(printer) {
            it.put(key1, value1)
        }.new {
            it.put(key2, value2)
        }
        logger.info("") {
            it.put(key3, value3)
        }
        assertEquals(
            listOf(
                Logger.Entry.Str(key1, value1),
                Logger.Entry.Str(key2, value2),
                Logger.Entry.Str(key3, value3)
            ),
            printer.values
        )
    }

    @Test
    fun lazy() {
        val printer = MockPrinter()
        val logger = Logger.new(
            printer,
            printMask = Logger.Level.printMask(Logger.Level.Error)
        )
        logger.info("") {
            throw IllegalStateException("must not me called")
        }
    }

    @Test
    fun printInt() {
        val key = "intKey"
        val value = 123
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Integer(key, value)), printer.values)
    }

    @Test
    fun printLong() {
        val key = "intKey"
        val value = 123L
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Lng(key, value)), printer.values)
    }

    @Test
    fun printFloat() {
        val key = "intKey"
        val value = 123f
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Flt(key, value)), printer.values)
    }

    @Test
    fun printDouble() {
        val key = "intKey"
        val value = 123.0
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Dbl(key, value)), printer.values)
    }

    @Test
    fun printBoolean() {
        val key = "intKey"
        val value = true
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(Logger.Entry.Booln(key, value)), printer.values)
    }

    @Test
    fun printInfo() {
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.info("")
        assertEquals(Logger.Level.Info, printer.level)
    }

    @Test
    fun printError() {
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.error("")
        assertEquals(Logger.Level.Error, printer.level)
    }

    @Test
    fun printDebug() {
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.debug("")
        assertEquals(Logger.Level.Debug, printer.level)
    }

    @Test
    fun printWarning() {
        val printer = MockPrinter()
        val logger = Logger.new(printer)
        logger.warning("")
        assertEquals(Logger.Level.Warning, printer.level)
    }

    private class MockPrinter : Logger.Printer {
        var level: Logger.Level? = null
        var owner: String? = null
        var message: String? = null
        var values: List<Logger.Entry> = emptyList()
        override fun printLog(
            level: Logger.Level,
            owner: String?,
            message: String,
            values: List<Logger.Entry>
        ) {
            this.level = level
            this.owner = owner
            this.message = message
            this.values = values
        }
    }
}