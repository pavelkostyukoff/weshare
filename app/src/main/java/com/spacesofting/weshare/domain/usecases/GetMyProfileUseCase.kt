package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.presentation.mvp.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetMyProfileUseCase {

    fun getAccount(): Observable<User>? {

        // вот это поменять на корутину
        return Api.Users.getAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}