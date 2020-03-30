package com.spacesofting.weshare.di

import com.spacesofting.weshare.common.DatabaseHelper
import com.spacesofting.weshare.common.NetworkUtils
import dagger.Component

@Component(modules = [StorageModule::class, NetworkModule::class])
interface AppComponent {
    val networkUtils: NetworkUtils?
    val databaseHelper: DatabaseHelper?
}