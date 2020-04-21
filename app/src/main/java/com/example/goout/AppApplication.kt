package com.example.goout

import android.app.Application
import com.example.goout.di.modules.appModules
import org.koin.core.context.startKoin

class AppApplication: Application() {
    /*override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModules)
        }
    } */
}