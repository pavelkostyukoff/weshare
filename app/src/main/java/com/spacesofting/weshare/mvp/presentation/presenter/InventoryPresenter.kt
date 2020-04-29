package com.spacesofting.weshare.mvp.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.mvp.presentation.view.InventoryView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class InventoryPresenter : MvpPresenter<InventoryView>() {
    var newId : String = ""

    fun creatNewAdvert() {
        Api.Adverts.creatNewAdvert()

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                newId = it.id.toString()
                viewState.openCreateFragment(newId)
            }) {
               it
            }
    }

}
