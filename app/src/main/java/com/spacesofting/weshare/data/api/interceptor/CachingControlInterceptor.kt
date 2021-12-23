package com.spacesofting.weshare.data.api.interceptor


import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET


class CachingControlInterceptor : Interceptor {
    private val connected = true

    interface TestService {
        @get:GET("/cache/60")
        val testDate: Call<String?>?
    }

    private fun isConnected(): Boolean {
        return connected
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (isConnected()) {
            request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build()
        } else {
            request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
        }
        val response = chain.proceed(request)
        System.out.println("network: " + response.networkResponse())
        System.out.println("cache: " + response.cacheResponse())
       return response
    }
}