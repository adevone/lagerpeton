package io.adev.lagerpeton.example

import io.adev.lagerpeton.Lager

class SomeUseCase {

    fun execute(arg1: String, arg2: Boolean, l: Lager) {
        l.error("do not name args 'arg1' and 'arg2'") {
            it.put("arg1", arg1)
            it.put("arg2", arg2)
        }
    }
}