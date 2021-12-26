package com.spacesofting.weshare.data.api.picture

import com.spacesofting.weshare.presentation.mvp.model.AvatarPhoto
import com.spacesofting.weshare.presentation.mvp.model.Photo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PicturesService {

    //todo add images to server db
    @Multipart
    @POST("me/adverts/{advertId}/images")
    fun addPictureMyGood(@Path("advertId") advertId: String, @Part file: MultipartBody.Part): Observable<Photo> //MultipartBody.Part

    //todo удаляет image из сервера
    @DELETE("me/adverts/{advertId}/images/{imageId}")
    fun delPictureMyGood(@Path("advertId") advertId: String, @Path("imageId") imageId: String):  Observable<Response<Void>>

    //fun getAccount(): Observable<User>
    @Multipart
    @POST("me/avatar")
    fun sentAvatar(@Part file: MultipartBody.Part): Observable<AvatarPhoto>
}