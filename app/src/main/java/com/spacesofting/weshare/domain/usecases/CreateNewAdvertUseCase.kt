package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.mvp.ResponceProfile
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreateNewAdvertUseCase {

    fun execute(): Observable<ResponceProfile> {

        // вот это поменять на корутину
        return Api.Adverts.createNewAdvert()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}