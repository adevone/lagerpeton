package io.adev.lagerpeton

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class LoggerTests {

    @Test
    fun message() {
        val message = "test"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info(message)
        assertEquals(message, printer.message)
    }

    @Test
    fun printValue() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun globalValue() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, value)
            })
        logger.info("")
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun copyValue() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val globalLogger = Lager.create(printer, PrimitivesOnlyAccumulator)
        val logger = globalLogger.copy(onEachLog = {
            it.put(key, value)
        })
        logger.info("")
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun overrideGlobalInCopy() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, "notTest")
            }
        ).copy(
            onEachLog = {
                it.put(key, value)
            }
        )
        logger.info("")
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun overrideGlobalOnPrint() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, "notTest")
            })
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun overrideCopyOnPrint() {
        val key = "testKey"
        val value = "test"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, "notTest")
            }
        ).copy(
            onEachLog = {
                it.put(key, "notTest2")
            }
        )
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
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
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key1, value1)
            }
        ).copy(
            onEachLog = {
                it.put(key2, value2)
            }
        )
        logger.info("") {
            it.put(key3, value3)
        }
        assertEquals(
            listOf(
                key1 to value1,
                key2 to value2,
                key3 to value3
            ),
            printer.values
        )
    }

    @Test
    fun lazy() {
        val printer = MockPrinter()
        val logger = Lager.create(
            printer, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(Lager.ERROR),
            onEachLog = {
                throw IllegalStateException("global append must not me called")
            }
        ).copy(
            onEachLog = {
                throw IllegalStateException("copy append must not me called")
            }
        )
        logger.info("") {
            throw IllegalStateException("message append must not me called")
        }
    }

    @Test
    fun globalValueChanged() {
        val key = "testKey"
        var value = "notAValue"
        val expectedValue = "aValue"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, value)
            })
        value = expectedValue
        logger.info("")
        assertEquals(listOf(key to expectedValue), printer.values)
    }

    @Test
    fun copyValueChanged() {
        val key = "testKey"
        var value = "notAValue"
        val expectedValue = "aValue"
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
            .copy(onEachLog = {
                it.put(key, value)
            })
        value = expectedValue
        logger.info("")
        assertEquals(listOf(key to expectedValue), printer.values)
    }

    @Test
    fun globalValueStored() {
        val key = "testKey"
        val expectedValue = "aValue"
        var value = expectedValue
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator) {
            it.put(key, value)
        }
        value = "notAValue"
        logger.info("")
        assertEquals(listOf(key to expectedValue), printer.values)
    }

    @Test
    fun copyValueStored() {
        val key = "testKey"
        val expectedValue = "aValue"
        var value = expectedValue
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator).copy {
            it.put(key, value)
        }
        value = "notAValue"
        logger.info("")
        assertEquals(listOf(key to expectedValue), printer.values)
    }

    @Test
    fun printInt() {
        val key = "testKey"
        val value = 123
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun printLong() {
        val key = "testKey"
        val value = 123L
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun printFloat() {
        val key = "testKey"
        val value = 123f
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun printDouble() {
        val key = "testKey"
        val value = 123.0
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun printBoolean() {
        val key = "testKey"
        val value = true
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), printer.values)
    }

    @Test
    fun printInfo() {
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.info("")
        assertEquals(Lager.INFO, printer.level)
    }

    @Test
    fun printError() {
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.error("")
        assertEquals(Lager.ERROR, printer.level)
    }

    @Test
    fun printDebug() {
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.debug("")
        assertEquals(Lager.DEBUG, printer.level)
    }

    @Test
    fun printWarning() {
        val printer = MockPrinter()
        val logger = Lager.create(printer, PrimitivesOnlyAccumulator)
        logger.warning("")
        assertEquals(Lager.WARNING, printer.level)
    }

    @Test
    fun printCustomLevel() {
        val level = 666
        val printer = MockPrinter()
        val logger = Lager.create(
            printer, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(level)
        )
        logger.log(level, "")
        assertEquals(level, printer.level)
    }

    @Test
    fun dontPrintInfo() {
        val printer = MockPrinter()
        val logger = Lager.create(
            printer, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.INFO))
        )
        logger.info("")
        assertFalse(printer.wasPrinted)
    }

    @Test
    fun dontPrintError() {
        val printer = MockPrinter()
        val logger = Lager.create(
            printer, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.ERROR))
        )
        logger.error("")
        assertFalse(printer.wasPrinted)
    }

    @Test
    fun dontPrintDebug() {
        val printer = MockPrinter()
        val logger = Lager.create(
            printer, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.DEBUG))
        )
        logger.debug("")
        assertFalse(printer.wasPrinted)
    }

    @Test
    fun dontPrintWarning() {
        val printer = MockPrinter()
        val logger = Lager.create(
            printer, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.WARNING))
        )
        logger.warning("")
        assertFalse(printer.wasPrinted)
    }

    private fun levelsWithout(level: Int): IntArray {
        return Lager.allLevels.filter { it != level }.toIntArray()
    }

    private class MockPrinter : TypedLager.Printer<PrimitivesOnlyAccumulator> {
        var wasPrinted = false
        var level: Int? = null
        var owner: String? = null
        var message: String? = null
        var values: List<Any> = emptyList()

        override fun printLog(
            level: Int,
            owner: String?,
            message: String,
            accumulator: PrimitivesOnlyAccumulator
        ) {
            this.wasPrinted = true
            this.level = level
            this.owner = owner
            this.message = message
            this.values = accumulator.values.map { (key, value) -> key to value }
        }
    }
}