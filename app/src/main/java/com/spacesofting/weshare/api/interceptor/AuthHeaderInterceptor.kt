package com.spacesofting.weshare.api.interceptor


import android.util.Log
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Refresh
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {

    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response? {
        var accessToken = "Bearer " + Settings.AccessToken
        var request: Request = chain.request()
        val response: Response = chain.proceed(request)

        val builder: Request.Builder = request.newBuilder()
        //todo первый раз прокатываем
        builder.header("Authorization", accessToken)// accessToken.getToken())
        builder.header("Content-Type", "application/json")
        builder.method(request.method(), request.body())
        request = builder.build()
        if (response.code() === 403) {
            //todo получаем 403
            synchronized(this) {
                Log.e(
                    "refreshToken",
                    "Failed " + request.toString()
                        .toString() + " with token -> " + accessToken // accessToken.getToken()
                )
                //todo получаем старый токен сюда
                val currentToken: String? =
                    "Bearer " + Settings.AccessToken // accessToken.getToken()
                //todo если верный токен не null и верный как и главный то
                if (currentToken != null && currentToken == accessToken) {
                    try {
                        //todo получаем новую пару
                        getRefreshToken() //aysnc
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                if (Settings.AccessToken != null) {
                    //todo подставляем новый токен
                    //todo меняем хеддер
                    builder.header("Authorization", "Bearer " + Settings.AccessToken)
                    builder.header("Content-Type", "application/json")
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
                    //todo проигрываем старый запрос
                    return chain.proceed(request)
                }
            }
        }
        Log.i("refreshToken", "req 200 with token -> " + accessToken)
        //todo возвращаем ответ
        return response
    }

    @Synchronized
    @Throws(InterruptedException::class)
    fun getRefreshToken() {
        if (!isRefreshing) {
            isRefreshing = true
            val refreshToken = Settings.refreshToken
            refreshToken?.let { it ->
                Api.Auth.getNewToken(Refresh(it))
                    .map {
                        Settings.AccessToken = it.accessToken
                        Settings.refreshToken = it.rowrefreshTokenVersion
                        isRefreshing = false
                    }
                    //todo запрашиваем в основном потоке , потому что нам нужно подменить токен в моменте
                    .blockingGet()
            }
        }
    }
}