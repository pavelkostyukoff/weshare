package com.spacesofting.weshare.api.picture

import com.spacesofting.weshare.mvp.Image
import com.spacesofting.weshare.mvp.model.Photo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PicturesService {

    @Multipart
    @POST("users/me/avatar")
    fun addPicture(@Part image: MultipartBody.Part): Observable<Photo>
}