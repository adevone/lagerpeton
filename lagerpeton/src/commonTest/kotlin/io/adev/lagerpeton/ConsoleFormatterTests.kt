package io.adev.lagerpeton

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ConsoleFormatterTests {

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
    fun ownerAndThrowable() {
        val owner = "testOwner"
        val e = try {
            throw RuntimeException()
        } catch (e: Exception) {
            e
        }
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator, owner = owner)
        logger.info("", throwable = e)
        assertEquals("$owner: stacktrace=${e.stackTraceToString()}", collector.logMessage)
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
    fun messageAndOwnerAndThrowable() {
        val owner = "testOwner"
        val message = "testMessage"
        val e = try {
            throw RuntimeException()
        } catch (e: Exception) {
            e
        }
        val collector = TestCollector()
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator, owner = owner)
        logger.info(message, throwable = e)
        assertEquals("$owner: $message, stacktrace=${e.stackTraceToString()}", collector.logMessage)
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

    @Test
    fun messageAndOwnerWithParamsAndThrowable() {
        val owner = "testOwner"
        val message = "testMessage"
        val key1 = "testKey1"
        val value1 = "testValue1"
        val key2 = "testKey2"
        val value2 = "testValue2"
        val collector = TestCollector()
        val e = try {
            throw RuntimeException()
        } catch (e: Exception) {
            e
        }
        val logger = Lager.create(collector, PrimitivesOnlyAccumulator, owner = owner)
            .copy(onEachLog = {
                it.put(key2, value2)
            })
        logger.info(message, throwable = e) {
            it.put(key1, value1)
        }
        assertEquals("$owner: $message, $key1=$value1, $key2=$value2, stacktrace=${e.stackTraceToString()}", collector.logMessage)
    }

    private class TestCollector : TypedLager.Collector<PrimitivesOnlyAccumulator> {
        var logMessage: String? = null

        override fun printLog(
            level: Int,
            owner: String?,
            message: String,
            throwable: Throwable?,
            accumulator: PrimitivesOnlyAccumulator
        ) {
            logMessage = ConsoleCollector.Formatter.format(owner, message, throwable, accumulator)
        }
    }
}