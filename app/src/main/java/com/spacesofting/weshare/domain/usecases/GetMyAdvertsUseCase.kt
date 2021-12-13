package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.data.api.ResponceMyAdvert
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetMyAdvertsUseCase {

    fun getMyAdverts(): Observable<ResponceMyAdvert> {

        // вот это поменять на корутину
        return    Api.Adverts.getMeAdverts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}