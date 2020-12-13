package io.adev.lagerpeton.example

class MainViewModel {
    private val someUseCase = SomeUseCase()

    private var isSwitched: Boolean = false

    private val logger = ServicesLocator.globalLogger.new(
        owner = MainViewModel::class.java.simpleName
    ) {
        it.put("isSwitched", isSwitched)
    }

    fun sendEventClicked() {
        isSwitched = !isSwitched
        logger.info("isSwitched is switched") {
            it.put("on", "event sent")
        }
    }

    fun runUseCaseClicked() {
        val l = logger.new {
            it.put("eventName", "runUseCaseClicked")
        }
        someUseCase.execute(arg1 = "test", arg2 = true, l)
    }
}