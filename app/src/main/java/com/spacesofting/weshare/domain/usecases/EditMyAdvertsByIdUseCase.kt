package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.presentation.mvp.model.Advert
import com.spacesofting.weshare.presentation.mvp.model.RespounceDataMyAdverts
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