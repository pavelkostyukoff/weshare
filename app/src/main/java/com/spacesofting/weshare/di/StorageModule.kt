package com.spacesofting.weshare.di

import com.spacesofting.weshare.common.DatabaseHelper
import dagger.Module
import dagger.Provides


@Module
class StorageModule {
    @Provides
    fun provideDatabaseHelper(): DatabaseHelper? {
        return DatabaseHelper()
    }
}