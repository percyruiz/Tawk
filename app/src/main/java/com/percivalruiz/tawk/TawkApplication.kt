package com.percivalruiz.tawk

import android.app.Application
import com.percivalruiz.tawk.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TawkApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TawkApplication)
            modules(appModule)
        }
    }
}