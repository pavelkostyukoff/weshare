package com.spacesofting.weshare.presentation.view

import com.spacesofting.weshare.data.api.Entitys
import com.spacesofting.weshare.data.api.RespouncePublish
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.io.File

@StateStrategyType(value = OneExecutionStateStrategy::class, tag = "")

interface AddGoodsView : MvpView {
    fun showWishImage(file: File)
    fun showToast(stringId: Int)
    fun showMaterialDialog(stringId: Int)
    fun wishImageDelete ()
    fun showForgetWriteAdress ()
    fun setNewSubCategory(it: Entitys)
  /*  fun setLoadedWish(wish: Wish)
    fun share(shareLink: ShareLink)*/

    fun save()
    fun cancel()
    fun showProgress(isShowed: Boolean)

    fun setConfirmButtonState(isEnabled: Boolean)
    fun saved(isSuccess: RespouncePublish)

    fun openCamera(file: File)
    fun openGallery()

/*
    fun showYMButton()
*/
    fun emptyPrice(isEmpty: Boolean)
    fun emptyTitle(isEmpty: Boolean)
    fun emptyDesc(isEmpty: Boolean)
    fun setPreviewImg(file: File)
    fun goToRegistration()
    fun showBrokenUrlMessage(isShowed: Boolean)
    fun showCheckUrlProgress(isShowed: Boolean)
    fun showClearUrlBtn(isShowed: Boolean)
    fun showAutoCompleteProgress(isShowed: Boolean)
/*
    fun goToAddCardFragment()
    fun goToRegisterFragment()*/
}
