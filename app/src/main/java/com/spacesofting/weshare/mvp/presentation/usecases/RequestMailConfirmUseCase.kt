package com.spacesofting.weshare.mvp.presentation.usecases

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.mvp.ResponceProfile
import com.spacesofting.weshare.mvp.model.PasswordResetComfirm
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class RequestMailConfirmUseCase {

    fun execute(passComf: PasswordResetComfirm): Observable<Response<Void>>? {

        // вот это поменять на корутину
        return Api.Users.reqestMailComfirm(passComf)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}