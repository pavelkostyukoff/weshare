package com.spacesofting.weshare.api.auth

import android.provider.ContactsContract
import com.spacesofting.weshare.mvp.Mail
import com.spacesofting.weshare.mvp.Registration

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("users/login")  //todo //Подтвердить получение задачи
    fun register(@Body mail: Mail): Observable<Registration>

   /* @POST("auth/otp")
    fun register(@Body registration: Registration, @Header("X-Confirmation-Code") sms: String): Observable<AuthResult>
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