package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.presentation.mvp.model.PasswordResetComfirm
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