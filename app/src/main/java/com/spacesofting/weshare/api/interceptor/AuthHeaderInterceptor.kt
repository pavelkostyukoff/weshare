package com.spacesofting.weshare.api.interceptor

import android.widget.Toast
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.Settings
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {
    companion object {
        val ACCESS = "Authorization"
        val VALIDATION = "X-Validation-Token"
    }

    var accessToken: String? = null
    var validationToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()

        val response = chain.proceed(request)
        if (response.code() == 403) {
            doSomething()
        }

        accessToken = "Bearer " + Settings.AccessToken
        validationToken = "Bearer " + Settings.ValidationToken

        if (!accessToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(ACCESS, accessToken).build()
        } else if (!validationToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(VALIDATION, validationToken).build()
        }

        return chain.proceed(request)
    }



    fun doSomething ()
    {
        Settings.AccessToken = Settings.ValidationToken
        Toast.makeText(ApplicationWrapper.context, "Ваша сессия истекла и была продлена автоматически  - выполните запрос еще раз!", Toast.LENGTH_LONG).show()
    }
}