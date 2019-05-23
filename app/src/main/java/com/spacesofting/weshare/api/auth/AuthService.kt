package com.digitalhorizon.eve.api.auth

import com.digitalhorizon.eve.mvp.model.Pass
import com.digitalhorizon.eve.mvp.model.Phone
import com.digitalhorizon.eve.mvp.model.StafferCard
import com.digitalhorizon.eve.mvp.model.auth.AuthResult
import com.digitalhorizon.eve.mvp.model.auth.Registration
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @POST("auth/otp")
    fun register(@Body registration: Registration): Observable<Completable>

    @POST("auth/otp")
    fun register(@Body registration: Registration, @Header("X-Confirmation-Code") sms: String): Observable<AuthResult>
    @PUT("event/pass/accept")
    fun passGuest(@Body pass: Pass): Observable<Response<Void>>
    /**
     * Response<Void> for response with empty body
     */
    //TODO: refactoring, set completable
    @POST("auth/otp/check")
    fun checkPhone(@Body phone: Phone): Observable<Response<Void>>

    //TODO: refactoring, set completable
    @DELETE("auth")
    fun logout(): Observable<Response<Void>>

    @GET("/api/v1/auth/userIdentity")
    fun getStafferCard(): Observable<StafferCard>
}