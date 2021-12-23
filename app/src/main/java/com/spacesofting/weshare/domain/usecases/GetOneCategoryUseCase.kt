package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.data.api.ResponceMyAdvertMaps
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class GetOneCategoryUseCase {

    fun getCategory(
        longitude: String,
        latitude: String,
        searchRadius: String,
        limit: Int,
        categoryId: String
    ): Observable<ResponceMyAdvertMaps>? {

        // вот это поменять на корутину
        return       Api.Tags.get8ItemToFeed(longitude,latitude,searchRadius,limit,categoryId)
            .observeOn(AndroidSchedulers.mainThread())
    }
}