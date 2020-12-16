package io.adev.lagerpeton

import kotlinx.serialization.json.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinxSerializationCollectorTests {

    @Test
    fun message() {
        val message = "testMessage"
        val owner = "testOwner"
        var loggedJson: JsonObject? = null
        val logger = Lager.kotlinxSerialization(
            collect = { json ->
                loggedJson = json
            }
        )
        logger.info(message, owner) {
            it.put("arg1", 123)
            it.put("arg2", true)
        }
        val expected = """{"arg1":123,"arg2":true,"level":"info","from":"$owner","message":"$message"}"""
        assertEquals(expected, loggedJson?.toString())
    }
}