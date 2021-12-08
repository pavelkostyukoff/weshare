package com.spacesofting.weshare.mvp.presentation.usecases

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.ResponceEditAdvert
import com.spacesofting.weshare.mvp.model.Advert
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateMyAdvertByIdUseCase {

    fun execute(advert: Advert, s: String): Observable<ResponceEditAdvert>? {

        // вот это поменять на корутину
        return   Api.Adverts.updateMyAdvertById(advert,s)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}