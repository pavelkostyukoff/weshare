package com.spacesofting.weshare.api.interceptor


import android.os.Process
import android.util.Log
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Refrash
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {


    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response? {
         val accessToken = "Bearer " + Settings.AccessToken


        val original: Request = chain.request()
        var request: Request = chain.request()
        val builder: Request.Builder = request.newBuilder()
        builder.header("Authorization", accessToken)// accessToken.getToken())
        builder.header("Content-Type", "application/json")
        builder.method(original.method(), original.body())
        val token: String? = accessToken // accessToken.getToken()
        request = builder.build()
        val response: Response = chain.proceed(request)
        if (response.code() === 403) {
            synchronized(this) {
                Log.e(
                    "refreshToken",
                    "Failed " + request.toString()
                        .toString() + " with token -> " + accessToken // accessToken.getToken()
                )
                val currentToken: String? = accessToken // accessToken.getToken()
                if (currentToken != null && currentToken == token) {
                    try {
                        isRefreshing = false
                        getRefreshToken() //aysnc
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                if (accessToken != null) {
                    builder.header("Authorization","Bearer " + accessToken)
                    request = builder.build()
                    Log.e(
                        "refreshToken",
                        "Send " + request.toString()
                            .toString() + " again with new token -> " + accessToken
                    )
                    Log.e(
                        "refreshToken",
                        "--------------------------------------------------------------------------------"
                    )
                    return chain.proceed(request)
                }
            }
        }
        Log.i("refreshToken", "req 200 with token -> " + accessToken)
        return response
    }

    @Synchronized
    @Throws(InterruptedException::class)
    fun getRefreshToken() {
        if (!isRefreshing) {


            val refreshToken = Settings.ValidationToken
            //todo autorize
            val token = refreshToken?.let { Refrash(it) }
            token?.let {
                Api.Auth.getNewToken(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({ it->
                        Settings.AccessToken = it.accessToken
                        Settings.ValidationToken = it.rowrefreshTokenVersion
                        //  ApplicationWrapper.user = it.user!!
                        //todo тут кладем токен в сохранялки Settings

                        //todo rx get newtoken
                        isRefreshing = true
                        Log.e("refreshToken", "Refreshing token...")

                    }){
                        it
                        //todo 403

                        //  autorize(mail)
                    }
            }
        }
    }
}

/* companion object {
        val ACCESS = "Authorization"
        val VALIDATION = "X-Validation-Token"
    }

    var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        var request: Request = chain.request()

        accessToken = "Bearer " + Settings.AccessToken
*//* if (Settings.isAuthenticated()) {
            Settings.AccessTokenAnonymous
        } else {
            Settings.AccessToken
        }*//*

        if (!accessToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(ACCESS, accessToken).build()
        }

        if (accessToken.isNullOrEmpty()*//* || Settings.isAnonymousUser*//* && !Settings.ValidationToken.isNullOrEmpty()) {
            //use validation token if we don't have Access token
            val validationToken = "Bearer " + Settings.ValidationToken
            if (!validationToken.isNullOrEmpty()) {
                request = request.newBuilder().addHeader(VALIDATION, validationToken).build()
            }
        }

        return chain.proceed(request)
    }*/
