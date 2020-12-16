package io.adev.lagerpeton

import kotlin.test.Test
import kotlin.test.assertEquals

class ConsolePrinterTests {

    @Test
    fun message() {
        val message = "testMessage"
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info(message)
        assertEquals(message, collector.logMessage)
    }

    @Test
    fun owner() {
        val owner = "testOwner"
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator, owner = owner)
        logger.info("")
        assertEquals("$owner: ", collector.logMessage)
    }

    @Test
    fun messageWithParam() {
        val message = "testMessage"
        val key = "testKey"
        val value = "testValue"
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator)
        logger.info(message) {
            it.put(key, value)
        }
        assertEquals("$message, $key=$value", collector.logMessage)
    }

    @Test
    fun messageAndOwner() {
        val owner = "testOwner"
        val message = "testMessage"
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator, owner = owner)
        logger.info(message)
        assertEquals("$owner: $message", collector.logMessage)
    }

    @Test
    fun messageAndOwnerWithParam() {
        val owner = "testOwner"
        val message = "testMessage"
        val key = "testKey"
        val value = "testValue"
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator, owner = owner)
        logger.info(message) {
            it.put(key, value)
        }
        assertEquals("$owner: $message, $key=$value", collector.logMessage)
    }

    @Test
    fun messageAndOwnerWithParams() {
        val owner = "testOwner"
        val message = "testMessage"
        val key1 = "testKey1"
        val value1 = "testValue1"
        val key2 = "testKey2"
        val value2 = "testValue2"
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator, owner = owner)
            .copy(onEachLog = {
                it.put(key2, value2)
            })
        logger.info(message) {
            it.put(key1, value1)
        }
        assertEquals("$owner: $message, $key1=$value1, $key2=$value2", collector.logMessage)
    }

    private class TestCollector : TypedLager.Collector<PrimitivesOnlyAccumulator> {
        var logMessage: String? = null

        override fun printLog(
            level: Int,
            owner: String?,
            message: String,
            accumulator: PrimitivesOnlyAccumulator
        ) {
            logMessage = ConsoleCollector.Formatter.format(owner, message, accumulator)
        }
    }
}