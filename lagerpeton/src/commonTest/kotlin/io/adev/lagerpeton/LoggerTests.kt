package io.adev.lagerpeton

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class LoggerTests {

    @Test
    fun message() {
        val message = "test"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info(message)
        assertEquals(message, collector.message)
    }

    @Test
    fun printValue() {
        val key = "testKey"
        val value = "test"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun globalValue() {
        val key = "testKey"
        val value = "test"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, value)
            })
        logger.info("")
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun copyValue() {
        val key = "testKey"
        val value = "test"
        val collector = MockCollector()
        val globalLogger = Lager.create(collector, PrimitivesOnlyAccumulator)
        val logger = globalLogger.copy(onEachLog = {
            it.put(key, value)
        })
        logger.info("")
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun overrideGlobalInCopy() {
        val key = "testKey"
        val value = "test"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, "notTest")
            }
        ).copy(
            onEachLog = {
                it.put(key, value)
            }
        )
        logger.info("")
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun overrideGlobalOnPrint() {
        val key = "testKey"
        val value = "test"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, "notTest")
            })
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun overrideCopyOnPrint() {
        val key = "testKey"
        val value = "test"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator,
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
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun merge() {
        val key1 = "testKey1"
        val value1 = "test1"
        val key2 = "testKey2"
        val value2 = "test2"
        val key3 = "testKey3"
        val value3 = "test3"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator,
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
            collector.values
        )
    }

    @Test
    fun lazy() {
        val collector = MockCollector()
        val logger = Lager.create(
            collector, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(Lager.ERROR_LEVEL),
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
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator,
            onEachLog = {
                it.put(key, value)
            })
        value = expectedValue
        logger.info("")
        assertEquals(listOf(key to expectedValue), collector.values)
    }

    @Test
    fun copyValueChanged() {
        val key = "testKey"
        var value = "notAValue"
        val expectedValue = "aValue"
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
            .copy(onEachLog = {
                it.put(key, value)
            })
        value = expectedValue
        logger.info("")
        assertEquals(listOf(key to expectedValue), collector.values)
    }

    @Test
    fun globalValueStored() {
        val key = "testKey"
        val expectedValue = "aValue"
        var value = expectedValue
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator) {
            it.put(key, value)
        }
        value = "notAValue"
        logger.info("")
        assertEquals(listOf(key to expectedValue), collector.values)
    }

    @Test
    fun copyValueStored() {
        val key = "testKey"
        val expectedValue = "aValue"
        var value = expectedValue
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator).copy {
            it.put(key, value)
        }
        value = "notAValue"
        logger.info("")
        assertEquals(listOf(key to expectedValue), collector.values)
    }

    @Test
    fun printInt() {
        val key = "testKey"
        val value = 123
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun printLong() {
        val key = "testKey"
        val value = 123L
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun printFloat() {
        val key = "testKey"
        val value = 123f
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun printDouble() {
        val key = "testKey"
        val value = 123.0
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun printBoolean() {
        val key = "testKey"
        val value = true
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info("") {
            it.put(key, value)
        }
        assertEquals(listOf(key to value), collector.values)
    }

    @Test
    fun printInfo() {
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info("")
        assertEquals(Lager.INFO_LEVEL, collector.level)
    }

    @Test
    fun printError() {
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.error("")
        assertEquals(Lager.ERROR_LEVEL, collector.level)
    }

    @Test
    fun printDebug() {
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.debug("")
        assertEquals(Lager.DEBUG_LEVEL, collector.level)
    }

    @Test
    fun printWarning() {
        val collector = MockCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.warning("")
        assertEquals(Lager.WARNING_LEVEL, collector.level)
    }

    @Test
    fun printCustomLevel() {
        val level = 666
        val collector = MockCollector()
        val logger = Lager.create(
            collector, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(level)
        )
        logger.log(level, "")
        assertEquals(level, collector.level)
    }

    @Test
    fun dontPrintInfo() {
        val collector = MockCollector()
        val logger = Lager.create(
            collector, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.INFO_LEVEL))
        )
        logger.info("")
        assertFalse(collector.wasPrinted)
    }

    @Test
    fun dontPrintError() {
        val collector = MockCollector()
        val logger = Lager.create(
            collector, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.ERROR_LEVEL))
        )
        logger.error("")
        assertFalse(collector.wasPrinted)
    }

    @Test
    fun dontPrintDebug() {
        val collector = MockCollector()
        val logger = Lager.create(
            collector, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.DEBUG_LEVEL))
        )
        logger.debug("")
        assertFalse(collector.wasPrinted)
    }

    @Test
    fun dontPrintWarning() {
        val collector = MockCollector()
        val logger = Lager.create(
            collector, PrimitivesOnlyAccumulator,
            printMask = Lager.makePrintMask(*levelsWithout(Lager.WARNING_LEVEL))
        )
        logger.warning("")
        assertFalse(collector.wasPrinted)
    }

    private fun levelsWithout(level: Int): IntArray {
        return Lager.allLevels.filter { it != level }.toIntArray()
    }

    private class MockCollector : TypedLager.Collector<PrimitivesOnlyAccumulator> {
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