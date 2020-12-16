package io.adev.lagerpeton.example

import android.app.Application
import io.adev.lagerpeton.Lager
import io.adev.lagerpeton.android

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServicesLocator.globalLogger = Lager.android {
            it.put("versionName", BuildConfig.VERSION_NAME)
            it.put("versionCode", BuildConfig.VERSION_CODE)
            it.put("appName", getString(R.string.app_name))
        }
    }
}