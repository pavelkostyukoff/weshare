package com.spacesofting.weshare.mvp.presentation.presenter

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.presentation.view.InventoryView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class InventoryPresenter : MvpPresenter<InventoryView>() {
    var newId : String = ""
    val router = ApplicationWrapper.INSTANCE.getRouter()

    fun creatNewAdvert() {
        Api.Adverts.creatNewAdvert()

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                newId = it.id.toString()
                router.navigateTo(ScreenPool.ADD_GOODS, newId)

            }) {
               it
            }
    }

}
