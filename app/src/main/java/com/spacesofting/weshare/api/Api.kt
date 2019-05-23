package com.digitalhorizon.eve.api

import com.digitalhorizon.eve.api.auth.AuthService
import com.digitalhorizon.eve.api.interceptor.AuthHeaderInterceptor
import com.digitalhorizon.eve.api.interceptor.MockInterceptor
import com.digitalhorizon.eve.api.interceptor.SMSInterceptor
import com.digitalhorizon.eve.api.picture.PicturesService
import com.digitalhorizon.eve.api.user.UserService
import com.spacesofting.weshare.common.Settings
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Api {
    val MOCK = MockInterceptor()
    val SMS = SMSInterceptor()
    val LOG = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val AUTH = AuthHeaderInterceptor()

    val CLIENT: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(AUTH)
            .addInterceptor(MOCK)
            .addInterceptor(SMS)
            .addInterceptor(LOG)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    val RETROFIT: Retrofit = Retrofit.Builder()
            .baseUrl(Settings.ApiPath)
            .client(CLIENT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()

    val Auth: AuthService
        get() = RETROFIT.create(AuthService::class.java)

    val Users: UserService
        get() = RETROFIT.create(UserService::class.java)

    val Pictures: PicturesService
        get() = RETROFIT.create(PicturesService::class.java)
}