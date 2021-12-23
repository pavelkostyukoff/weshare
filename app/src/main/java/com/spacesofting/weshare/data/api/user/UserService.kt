package com.spacesofting.weshare.data.api.user

import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.ResponceProfile
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.Mail
import com.spacesofting.weshare.mvp.model.MailComfirm
import com.spacesofting.weshare.mvp.model.PasswordResetComfirm
import com.spacesofting.weshare.mvp.model.UpdateProfile
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    @POST("/auth/register")  //todo //Подтвердить получение задачи Observable<Response<Void>>
    fun register(@Body updateProfile: Profile): Observable<ResponceProfile>
    @POST("/auth/verify-email/request")  //todo //Запрос пароля напочту повторно
    fun verifyMailRequest(@Body mail: Mail?): Observable<Response<Void>>
    @POST("/auth/verify-email") //todo //Запрос пароля напочту
    fun comfirmMeil(@Body mailComfirm: MailComfirm?): Observable<Response<Void>>
    @POST("auth/reset-password/request")  //todo //Подтвердить получение задачи
    fun reqestMeil(@Body mail: Mail?): Observable<Response<Void>>
    @POST("auth/reset-password/check")  //todo //Подтвердить получение задачи
    fun reqestMailCHeck(@Body mailcomfirm: MailComfirm?): Observable<Response<Void>>
    @POST("auth/reset-password/confirm")  //todo //Подтвердить получение задачи
    fun reqestMailComfirm(@Body passComfirm: PasswordResetComfirm?): Observable<Response<Void>>
    //todo  Get user profile
    @GET("me")
    fun getAccount(): Observable<User>
    //todo  Update user profile
    @PATCH("me")
    fun updateProfile(@Body updProfile: UpdateProfile): Observable<User>
}