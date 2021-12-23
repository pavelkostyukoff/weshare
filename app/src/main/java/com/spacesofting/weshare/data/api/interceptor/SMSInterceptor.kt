package com.spacesofting.weshare.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class SMSInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}