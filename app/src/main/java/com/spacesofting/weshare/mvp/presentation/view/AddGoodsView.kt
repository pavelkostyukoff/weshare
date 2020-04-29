package com.spacesofting.weshare.mvp.presentation.view

import com.arellomobile.mvp.MvpView
import java.io.File

interface AddGoodsView : MvpView {
    fun showWishImage(file: File)
    fun showToast(stringId: Int)
    fun showMaterialDialog(stringId: Int)
    fun wishImageDelete ()
  /*  fun setLoadedWish(wish: Wish)
    fun share(shareLink: ShareLink)*/

    fun save()
    fun cancel()
    fun showProgress(isShowed: Boolean)

    fun setConfirmButtonState(isEnabled: Boolean)
    fun saved(isSuccess: Boolean)

    fun openCamera(file: File)
    fun openGallery()

/*
    fun showYMButton()
*/
    fun emptyPrice(isEmpty: Boolean)
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
