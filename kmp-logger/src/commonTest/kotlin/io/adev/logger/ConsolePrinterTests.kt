package io.adev.logger

import kotlin.test.Test
import kotlin.test.assertEquals

class ConsolePrinterTests {

    @Test
    fun message() {
        val message = "testMessage"
        val printer = TestPrinter()
        val logger = Logger.new(printer)
        logger.info(message)
        assertEquals(message, printer.logMessage)
    }

    @Test
    fun owner() {
        val owner = "testOwner"
        val printer = TestPrinter()
        val logger = Logger.new(printer, owner = owner)
        logger.info("")
        assertEquals("$owner: ", printer.logMessage)
    }

    @Test
    fun messageWithParam() {
        val message = "testMessage"
        val key = "testKey"
        val value = "testValue"
        val printer = TestPrinter()
        val logger = Logger.new(printer)
        logger.info(message) {
            it.put(key, value)
        }
        assertEquals("$message, $key=$value", printer.logMessage)
    }

    @Test
    fun messageAndOwner() {
        val owner = "testOwner"
        val message = "testMessage"
        val printer = TestPrinter()
        val logger = Logger.new(printer, owner = owner)
        logger.info(message)
        assertEquals("$owner: $message", printer.logMessage)
    }

    @Test
    fun messageAndOwnerWithParam() {
        val owner = "testOwner"
        val message = "testMessage"
        val key = "testKey"
        val value = "testValue"
        val printer = TestPrinter()
        val logger = Logger.new(printer, owner = owner)
        logger.info(message) {
            it.put(key, value)
        }
        assertEquals("$owner: $message, $key=$value", printer.logMessage)
    }

    @Test
    fun messageAndOwnerWithParams() {
        val owner = "testOwner"
        val message = "testMessage"
        val key1 = "testKey1"
        val value1 = "testValue1"
        val key2 = "testKey2"
        val value2 = "testValue2"
        val printer = TestPrinter()
        val logger = Logger.new(printer, owner = owner)
        logger.info(message) {
            it.put(key1, value1)
            it.put(key2, value2)
        }
        assertEquals("$owner: $message, $key1=$value1, $key2=$value2", printer.logMessage)
    }

    private class TestPrinter : Logger.Printer<PrimitivesOnlyAccumulator> {
        var logMessage: String? = null

        override fun printLog(
            level: Int,
            owner: String?,
            message: String,
            accumulator: PrimitivesOnlyAccumulator
        ) {
            logMessage = ConsolePrinter.Formatter.format(owner, message, accumulator)
        }

        override fun createAccumulator(): PrimitivesOnlyAccumulator {
            return PrimitivesOnlyAccumulator()
        }
    }
}