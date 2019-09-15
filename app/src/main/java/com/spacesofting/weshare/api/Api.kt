package com.spacesofting.weshare.api

import com.spacesofting.weshare.api.auth.AuthService
import com.spacesofting.weshare.api.interceptor.AuthHeaderInterceptor
import com.spacesofting.weshare.api.interceptor.MockInterceptor
import com.spacesofting.weshare.api.interceptor.SMSInterceptor
import com.spacesofting.weshare.api.picture.PicturesService
import com.spacesofting.weshare.api.picture.TagsService
import com.spacesofting.weshare.api.user.UserService
import com.spacesofting.weshare.common.Settings
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import android.R
import android.content.Context
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import javax.security.cert.CertificateException


object Api {
    val MOCK = MockInterceptor()
    val SMS = SMSInterceptor()
    val LOG = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val AUTH = AuthHeaderInterceptor()

    val CLIENT: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(AUTH)
          //  .addInterceptor(MOCK)
          //  .addInterceptor(SMS)
            .addInterceptor(LOG)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()


    val RETROFIT: Retrofit = Retrofit.Builder()
            .baseUrl(Settings.ApiPath)
            //.client(CLIENT)

        .client(getUnsafeOkHttpClient().build())

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


    fun getUnsafeOkHttpClient(): OkHttpClient.Builder

    {

        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) = Unit

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) = Unit
            })
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true
                }
            })
            builder.addInterceptor(AUTH)
            builder.addInterceptor(LOG)

            builder.connectTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.writeTimeout(30, TimeUnit.SECONDS)

            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

}