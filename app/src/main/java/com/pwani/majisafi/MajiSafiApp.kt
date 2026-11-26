package com.pwani.majisafi

import android.app.Application
import com.pwani.majisafi.data.worker.SalinitySyncWorker
import com.pwani.majisafi.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MajiSafiApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin Dependency Injection
        startKoin {
            androidLogger()
            androidContext(this@MajiSafiApp)
            modules(appModule)
        }

        // Schedule periodic background sync for offline readings
        SalinitySyncWorker.schedulePeriodicSync(this)
    }
}
