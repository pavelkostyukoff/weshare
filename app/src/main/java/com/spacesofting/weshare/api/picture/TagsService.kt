package com.spacesofting.weshare.api.picture

import com.spacesofting.weshare.api.Entitys
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TagsService {

    @GET("advert-categories")
    fun getCategories(/*@Query("id") id : String?,*/ @Query("parentId") parentId : String?,/* @Query("search") search : String?,*/ /*@Query("searchByCode") searchByCode : String?,*/ @Query("limit") limit : Int, @Query("offset") offset: Int): Observable<Entitys>
    //todo туту я должен получить список кол-во категорий

}