package io.adev.lagerpeton.example

import android.app.Application
import io.adev.lagerpeton.AndroidCollector
import io.adev.lagerpeton.PrimitivesOnlyAccumulator
import io.adev.lagerpeton.TypedLager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServicesLocator.globalLogger = TypedLager.create(
            AndroidCollector, PrimitivesOnlyAccumulator
        ) {
            it.put("versionName", BuildConfig.VERSION_NAME)
            it.put("versionCode", BuildConfig.VERSION_CODE)
            it.put("appName", getString(R.string.app_name))
        }
    }
}