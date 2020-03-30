package com.spacesofting.weshare.di

import com.spacesofting.weshare.common.NetworkUtils
import dagger.Module
import dagger.Provides


@Module
class NetworkModule {
    @Provides
    fun provideNetworkUtils(): NetworkUtils {
        return NetworkUtils()
    }
}