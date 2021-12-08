package com.spacesofting.weshare.mvp.presentation.usecases

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.UpdateProfile
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateProfileUseCase {

    fun update(updProfile: UpdateProfile): Observable<User>? {

        // вот это поменять на корутину
        return  Api.Users.updateProfile(updProfile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}