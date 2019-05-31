package com.spacesofting.weshare.api.picture

import android.media.Image
import com.spacesofting.weshare.mvp.model.Photo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PicturesService {

    @Multipart
    @POST("/api/v1/resources/picture")
    fun addPicture(@Part image: MultipartBody.Part): Observable<Photo>
}