package com.digitalhorizon.eve.common

import android.app.Application
import ru.terrakok.cicerone.Cicerone

class ApplicationWrapper : Application() {
    companion object {
        lateinit var INSTANCE: ApplicationWrapper
    }

    private lateinit var cicerone: Cicerone<EveRouter>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initCicerone()
    }

    private fun initCicerone(){
        cicerone = Cicerone.create(EveRouter())
    }

    fun getNavigationHolder() = cicerone.navigatorHolder

    fun getRouter() = cicerone.router
}