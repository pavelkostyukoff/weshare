package com.spacesofting.weshare.di

import com.spacesofting.weshare.mvp.ui.activity.MainActivity
import dagger.Component


@Component(modules = [StorageModule::class, NetworkModule::class])
interface AppComponent {
    fun injectsMainActivity(mainActivity: MainActivity?)
}
