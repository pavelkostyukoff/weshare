package com.spacesofting.weshare.mvp.presentation.usecases

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.mvp.model.Advert
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditMyAdvertsByIdUseCase {

    fun editMyAdvertById(itemRent: RespounceDataMyAdverts): Observable<Advert>? {

        // вот это поменять на корутину
        return     Api.Adverts.getMyAdvertById(itemRent.id!!)//todo take goods by id
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}