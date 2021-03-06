package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.data.api.ResponceMyAdvertMaps
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetMyAdvertToMapsUseCase {

    fun execute(lan: String, lat: String, searchRadius: String, category: String): Observable<ResponceMyAdvertMaps>? {

        // вот это поменять на корутину
        return  Api.Adverts.getMyAdverts(lan, lat, searchRadius, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}