package com.spacesofting.weshare.api.picture

import com.spacesofting.weshare.mvp.model.Photo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PicturesService {

    @Multipart
    @POST("me/adverts/{advertId}/images")
    fun addPictureMyGood(@Path("advertId") advertId: String, @Part file: MultipartBody.Part): Observable<Photo> //MultipartBody.Part

    @DELETE("me/adverts/{advertId}/images/{imageId}")  //todo //
    fun delPictureMyGood(@Path("advertId") advertId: String, @Path("imageId") imageId: String):  Observable<Response<Void>>

    //fun getAccount(): Observable<User>
 /*   @Multipart
    @POST("users/me/avatar")
    fun postFile(@Part file: MultipartBody.Part, @Part("description") description: RequestBody): Call<ResponseBody>*/




}