package com.spacesofting.weshare.api.picture

import com.spacesofting.weshare.mvp.Category
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Image
import com.spacesofting.weshare.mvp.model.Photo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface TagsService {

    @GET("categories")
    fun getListCompilations(@Query("limit") limit : Int, @Query("offset") offset: Int): Observable<Category>
    //todo туту я должен получить список кол-во категорий

}