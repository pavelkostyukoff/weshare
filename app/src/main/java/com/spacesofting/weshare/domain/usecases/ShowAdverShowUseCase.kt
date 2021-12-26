package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.presentation.mvp.Profile
import com.spacesofting.weshare.presentation.mvp.model.dto.AdvertModifiredRespouns
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