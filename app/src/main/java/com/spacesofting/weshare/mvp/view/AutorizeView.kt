package com.spacesofting.weshare.mvp.view

import com.arellomobile.mvp.MvpView

interface AutorizeView : MvpView {
    fun showProgress(isVisible: Boolean)
    fun showCheckPhoneProgress(isVisible: Boolean)
    fun showComfiermRequstMailDialog()
    fun shownewPasswordCreate()

    fun showErrorMessage(isVisible: Boolean, msg: Int = -1, countDown: Long = 0, isBlocked: Boolean = false)
    fun onCheckedPhone(isValid: Boolean)
    fun showCountDownRetrySms(countDown: Long)
    fun toastError (s : String)
}
