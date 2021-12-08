package com.spacesofting.weshare.mvp.presentation.usecases

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.model.dto.AdvertModifiredRespouns
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class ShowAdverShowUseCase(idAdvert: String) {
    private val idAdvert = idAdvert
    fun execute(profile: Profile): Observable<AdvertModifiredRespouns>? {

        // вот это поменять на корутину
        return     Api.Adverts.newgetMyAdvertById(idAdvert)
            .observeOn(AndroidSchedulers.mainThread())
    }
}