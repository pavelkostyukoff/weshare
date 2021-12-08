package com.spacesofting.weshare.mvp.presentation.usecases

import com.spacesofting.weshare.api.Api
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DeleteMyAdvertsByIdUseCase {

    fun deleteMyAdvertById(itemRent: String, goodImageId: String): Observable<Response<Void>>? {

        // вот это поменять на корутину
        return  Api.Pictures.delPictureMyGood(itemRent, goodImageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}