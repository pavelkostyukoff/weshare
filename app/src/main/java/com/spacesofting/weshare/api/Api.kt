package com.spacesofting.weshare.api

import com.spacesofting.weshare.api.auth.AuthService
import com.spacesofting.weshare.api.interceptor.*
import com.spacesofting.weshare.api.picture.PicturesService
import com.spacesofting.weshare.api.picture.TagsService
import com.spacesofting.weshare.api.user.AdvertsService
import com.spacesofting.weshare.api.user.UserService
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.context
import com.spacesofting.weshare.common.Settings
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


object Api {
    //cache url
    var httpCacheDirectory =
        File(context.cacheDir, "responses")
    var cacheSize = 10 * 1024 * 1024 // 10 MiB

    var cache = Cache(httpCacheDirectory, cacheSize.toLong())
    val MOCK = MockInterceptor()
    val REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = CachingControlInterceptor()
    val SMS = SMSInterceptor()
    private val LOG: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val AUTH = AuthHeaderInterceptor()
    private val TOKEN = TokenInterceptor()

    private val CLIENT: OkHttpClient = OkHttpClient.Builder().cache(
        Cache(
            File("/tmp/http"),
            10 * 1024 * 1024
        ))
        .addInterceptor(AUTH)
          .addInterceptor(MOCK)
       // .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)
        //  .addInterceptor(SMS)
        .addInterceptor(TOKEN)
        .addInterceptor(LOG)
        .cache(cache)
        /*.connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)*/
        .build()


    private val RETROFIT: Retrofit = Retrofit.Builder()
        .baseUrl(Settings.ApiPath)

        .client(CLIENT)
       // .client(getUnsafeOkHttpClient().build())
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

    val Tags: TagsService
        get() = RETROFIT.create(TagsService::class.java)

    val Adverts: AdvertsService
        get() = RETROFIT.create(AdvertsService::class.java)


 /*   private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) =
                    Unit

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) =
                    Unit
            })
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            return OkHttpClient.Builder()
                .addInterceptor(AUTH)
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(LOG)
                .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }*/
}