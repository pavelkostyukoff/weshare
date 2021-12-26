package com.spacesofting.weshare

import android.app.Application
import com.spacesofting.weshare.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                domainModule//todo , другие модули
            )
        }
    }
}