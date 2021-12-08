package com.spacesofting.weshare.mvp.presentation.usecases

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.RespouncePublish
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PublishMyAdvertUseCase {

    fun execute(goodId: String): Observable<RespouncePublish>? {

        // вот это поменять на корутину
        return Api.Adverts.publishMyAdvert(goodId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
    }
}