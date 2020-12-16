package io.adev.lagerpeton.example

class MainViewModel {
    private val someUseCase = SomeUseCase()

    private var counter: Int = 0

    private val logger = ServicesLocator.globalLogger.copy(
        owner = MainViewModel::class.java.simpleName,
        onEachLog = {
            it.put("countReactive", counter)
        }
    ) {
        it.put("countStored", counter)
    }

    fun updateCounterClicked() {
        counter += 1
        logger.info("counter updated") {
            it.put("on", "event sent")
        }
    }

    fun runUseCaseClicked() {
        val l = logger.copy {
            it.put("eventName", "runUseCaseClicked")
        }
        someUseCase.execute(arg1 = "test", arg2 = true, logger = l)
    }
}