package com.spacesofting.weshare.api.auth

import com.spacesofting.weshare.mvp.Mail
import com.spacesofting.weshare.mvp.Autorize
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.ResponceProfile

import io.reactivex.Observable
import retrofit2.http.*

interface AuthService {

    @POST("users/login")  //todo //Подтвердить получение задачи
    fun autorize(@Body mail: Mail): Observable<Autorize>

    @POST("/users/register")  //todo //Подтвердить получение задачи
    fun register(@Body profile: Profile): Observable<ResponceProfile>

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