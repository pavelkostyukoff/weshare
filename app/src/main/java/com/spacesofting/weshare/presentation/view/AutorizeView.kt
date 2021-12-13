package com.spacesofting.weshare.presentation.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface AutorizeView : MvpView {
    fun showProgress(isVisible: Boolean)
    fun showCheckPhoneProgress(isVisible: Boolean)
    fun showComfiermRequstMailDialog()
    fun shownewPasswordCreate()
    fun errorDlg(message: String)

    fun showErrorMessage(isVisible: Boolean, msg: Int = -1, countDown: Long = 0, isBlocked: Boolean = false)
    fun onCheckedPhone(isValid: Boolean)
    fun showCountDownRetrySms(countDown: Long)
    fun toastError (s : String)
}
