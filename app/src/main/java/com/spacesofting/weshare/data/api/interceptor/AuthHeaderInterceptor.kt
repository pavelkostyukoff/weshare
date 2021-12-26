package com.spacesofting.weshare.data.api.interceptor


import android.util.Log
import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.presentation.common.Settings
import com.spacesofting.weshare.presentation.mvp.Refresh
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {

    private var isRefreshing = false

    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = "Bearer " + Settings.accessToken
        var originalRequest : Request = chain.request()
        val response: Response = chain.proceed(originalRequest )

        val builder: Request.Builder = originalRequest.newBuilder()
        //todo первый раз прокатываем
        builder.header("Authorization", accessToken)// accessToken.getToken())
        builder.header("Content-Type", "application/json")
        builder.method(originalRequest.method, originalRequest.body)
        originalRequest  = builder.build()
        if (response.code.equals(403)/* || response.code() === 401*/) {
            //todo получаем 403
            synchronized(this) {
                Log.e(
                    "refreshToken",
                    "Failed " + originalRequest .toString()
                        .toString() + " with token -> " + accessToken // accessToken.getToken()
                )
                //todo получаем старый токен сюда
                val currentToken: String? =
                    "Bearer " + Settings.accessToken // accessToken.getToken()
                //todo если верный токен не null и верный как и главный то
                        getRefreshToken(Settings.refreshToken) // в параметр кладем рефрешь
                if (Settings.accessToken != null) {
                    //todo подставляем новый токен
                    //todo меняем хеддер
                    builder.header("Authorization", "Bearer " + Settings.accessToken)
                    builder.header("Content-Type", "application/json")
                    originalRequest  = builder.build()
                    Log.e("refreshToken", "Send " + originalRequest .toString().toString() + " again with new token -> " + accessToken)
                    Log.e("refreshToken", "--------------------------------------------------------------------------------")
                    //todo проигрываем старый запрос
                    return chain.proceed(originalRequest )
                }
            }
        }
        Log.i("refreshToken", "req 200 with token -> " + accessToken)
        //todo возвращаем ответ
        return response
    }

    @Synchronized
    @Throws(InterruptedException::class)
    fun getRefreshToken(refreshToken: String?) {
        if (!isRefreshing) {
            isRefreshing = true
            refreshToken?.let { it ->
             val newPair =    Api.Auth.getNewToken(Refresh(it)).blockingGet()
                        Settings.accessToken = newPair.accessToken
                        Settings.refreshToken = newPair.refreshToken
                Log.i("refreshToken______NEW", "req 200 with token -> " + newPair.accessToken)
                        isRefreshing = false
                    //todo запрашиваем в основном потоке , потому что нам нужно подменить токен в моменте

            }
        }
    }
}