package com.spacesofting.weshare.api.interceptor

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

        accessToken = "Bearer " + Settings.AccessToken
        validationToken = "Bearer " + Settings.ValidationToken

        if (!accessToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(ACCESS, accessToken).build()
        } else if (!validationToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(VALIDATION, validationToken).build()
        }

        return chain.proceed(request)
    }
}