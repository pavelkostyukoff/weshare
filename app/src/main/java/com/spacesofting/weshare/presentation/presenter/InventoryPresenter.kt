package com.spacesofting.weshare.presentation.presenter

import android.annotation.SuppressLint
import com.spacesofting.weshare.domain.common.ApplicationWrapper
import com.spacesofting.weshare.domain.common.ApplicationWrapper.Companion.editAdvertId
import com.spacesofting.weshare.domain.common.ScreenPool
import com.spacesofting.weshare.domain.usecases.CreateNewAdvertUseCase
import com.spacesofting.weshare.presentation.view.InventoryView
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
