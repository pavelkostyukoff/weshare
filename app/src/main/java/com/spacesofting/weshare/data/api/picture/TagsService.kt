package com.spacesofting.weshare.data.api.picture

import com.spacesofting.weshare.data.api.Entitys
import com.spacesofting.weshare.data.api.ResponceMyAdvertMaps
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TagsService {

    @GET("advert-categories")
    fun getCategories(/*@Query("id") id : String?,*/ @Query("parentId") parentId : String?,/* @Query("search") search : String?,*/ /*@Query("searchByCode") searchByCode : String?,*/ @Query("limit") limit : Int, @Query("offset") offset: Int): Observable<Entitys>
    //todo туту я должен получить список кол-во категорий
    //get advert by  category to feed
    //todo  Get user profile
    @GET("adverts")
    fun get8ItemToFeed(@Query("longitude") longitude: String,
                     @Query("latitude") latitude: String,
                     @Query("searchRadius") searchRadius: String,
                     @Query("limit") limit: Int,
                     @Query("categoryId") categoryId: String ): Observable<ResponceMyAdvertMaps>
}