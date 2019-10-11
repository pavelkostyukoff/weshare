package com.spacesofting.weshare.api.picture

import com.spacesofting.weshare.mvp.model.Photo
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Multipart

interface PicturesService {

    @Multipart
    @PATCH("users/me/avatar")
    fun addPicture(@Part file: MultipartBody.Part): Observable<Photo> //MultipartBody.Part

    @DELETE("users/me/avatar")  //todo //
    fun delPicture():  Observable<Response<Void>>

    //fun getAccount(): Observable<User>
 /*   @Multipart
    @POST("users/me/avatar")
    fun postFile(@Part file: MultipartBody.Part, @Part("description") description: RequestBody): Call<ResponseBody>*/




}