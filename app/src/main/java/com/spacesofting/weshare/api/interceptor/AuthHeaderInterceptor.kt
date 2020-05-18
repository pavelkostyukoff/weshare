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

    private var accessToken: String? = null
    private var validationToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()

        accessToken = "Bearer " + Settings.AccessToken
        validationToken = Settings.ValidationToken

        if (!accessToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(ACCESS, accessToken).build()
        } else if (!validationToken.isNullOrEmpty()) {
            request = request.newBuilder().addHeader(VALIDATION, validationToken).build()
        }

        return chain.proceed(request)
    }
   /* companion object {
        val ACCESS = "Authorization"
        val VALIDATION = "X-Validation-Token"
    }

    var accessToken: String? = null
    var validationToken: String? = null
    val router = ApplicationWrapper.instance.getRouter()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()

        val response = chain.proceed(request)
        if (response.code() == 403) {
           // doSomething()
            router.navigateTo(ScreenPool.REGISTRATION_FRAGMENT)
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
     //   Toast.makeText(context, "Ваша сессия истекла и была продлена автоматически  - выполните запрос еще раз!", Toast.LENGTH_LONG).show()
        Settings.AccessToken = Settings.ValidationToken
      //  ApplicationWrapper.mailAutorize
       // toast("Ваша сессея обновлена,попробуйте еще раз!")
        Api.Auth.autorize(ApplicationWrapper.mailAutorize)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                    it->
                Settings.AccessToken = it.token
                Settings.ValidationToken = it.rowrefreshTokenVersion
                ApplicationWrapper.user = it.user!!
                router.navigateTo(ScreenPool.EDIT_PROFILE)
            }){
                it
            }
    }*/
}