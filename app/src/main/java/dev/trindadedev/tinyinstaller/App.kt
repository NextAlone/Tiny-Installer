package dev.trindadedev.tinyinstaller

import android.app.Application

import dev.trindadedev.tinyinstaller.di.init.appModules

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        CrashHandler.init()
        super.onCreate()
        startKoin {
            // Koin Android Logger
            androidLogger()
            // Koin Android Context
            androidContext(this@App)
            // use modules
            modules(appModules)
        }
    }
}
