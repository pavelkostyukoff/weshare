package com.spacesofting.weshare.common

import android.app.Application
import com.spacesofting.weshare.mvp.User
import ru.terrakok.cicerone.Cicerone

class ApplicationWrapper : Application() {
    companion object {
        lateinit var INSTANCE: ApplicationWrapper
        lateinit var user: User

    }

    private lateinit var cicerone: Cicerone<Boomerango>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        user = User()
        initCicerone()
    }

    private fun initCicerone() {
        cicerone = Cicerone.create(Boomerango())
    }

    fun getNavigationHolder() = cicerone.navigatorHolder
    fun getRouter() = cicerone.router

    fun restartApp() {
        System.exit(0)
    }
}