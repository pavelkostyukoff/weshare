package com.spacesofting.weshare.mvp.presentation.presenter

import android.annotation.SuppressLint
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.editAdvertId
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.presentation.usecases.CreateNewAdvertUseCase
import com.spacesofting.weshare.mvp.presentation.view.InventoryView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class InventoryPresenter : MvpPresenter<InventoryView>() {
    val useCase = CreateNewAdvertUseCase()
    var newId : String = ""
    val router = ApplicationWrapper.instance.getRouter()

    @SuppressLint("CheckResult")
    fun createNewAdvert() {
        router.navigateTo(ScreenPool.ADD_GOODS, editAdvertId)
        useCase.execute()
            .subscribe({
                editAdvertId = it.id.toString()

            }) {

            }
    }
}
