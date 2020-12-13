[Lagerpeton](https://en.wikipedia.org/wiki/Lagerpeton) is an ancient reptile lived ~236–234 million years ago. Any reference to class [Lager](https://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html) and to language [Peton](https://www.python.org/) is purely coincidental

## What the library for

Lagerpeton allows accumulation of context for logging.

Example:
```
// In App class
val globalLogger = Logger.new(AndroidPrinter) {
    it.put("appVersion", "1.0.0")
    it.put("platform", "1.0.0")
}

class SomeViewModel {

    // In class
    val classLogger = globalLogger.new {
        // always will get current value because lambda evaluates on each logging
        it.put("state", someState)
    }

    fun eventHappened(arg1: String, arg2: String) {
        // On logging
        classLogger.info("something happened") {
            it.put("arg1", arg1)
            it.put("arg2", arg2)
        }
    }
}
```