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

    override fun intercept(chain: Interceptor.Chain): Response {

        var request: Request = chain.request()

        accessToken = "Bearer " + Settings.AccessToken
/* if (Settings.isAuthenticated()) {
            Settings.AccessTokenAnonymous
        } else {
            Settings.AccessToken
        }*/

        if (!accessToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(ACCESS, accessToken).build()
        }

        if (accessToken.isNullOrEmpty()/* || Settings.isAnonymousUser*/ && !Settings.ValidationToken.isNullOrEmpty()) {
            //use validation token if we don't have Access token
            val validationToken = Settings.ValidationToken
            if (!validationToken.isNullOrEmpty()) {
                request = request.newBuilder().addHeader(VALIDATION, validationToken).build()
            }
        }

        return chain.proceed(request)
    }
}