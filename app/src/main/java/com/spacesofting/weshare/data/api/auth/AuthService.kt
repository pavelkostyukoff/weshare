package com.spacesofting.weshare.data.api.auth

import com.spacesofting.weshare.presentation.mvp.Autorize
import com.spacesofting.weshare.presentation.mvp.Login
import com.spacesofting.weshare.presentation.mvp.Refresh
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")  //todo //Подтвердить получение задач`и
    fun autorize(@Body mail: Login): Observable<Autorize>

    @POST("auth/refresh-token")  //todo Refresh token
    fun getNewToken(@Body refreshToken: Refresh): Single<Autorize>

   /* @POST("auth/otp")
    fun register(@Body registration: Autorize, @Header("X-Confirmation-Code") sms: String): Observable<AuthResult>
    @PUT("event/pass/accept")
    fun passGuest(@Body pass: Pass): Observable<Response<Void>>

    //TODO: refactoring, set completable
    @POST("auth/otp/check")
    fun checkPhone(@Body phone: ContactsContract.CommonDataKinds.Phone): Observable<Response<Void>>

    //TODO: refactoring, set completable
    @DELETE("auth")
    fun logout(): Observable<Response<Void>>

    @GET("/api/v1/auth/userIdentity")
    fun getStafferCard(): Observable<StafferCard>*/
}