package com.spacesofting.weshare.ui.fragment.presentation.view

import com.arellomobile.mvp.MvpView

interface RegistrationView : MvpView {
    fun showProgress(isVisible: Boolean)
    fun showCheckPhoneProgress(isVisible: Boolean)
    fun showErrorMessage(isVisible: Boolean, msg: Int = -1, countDown: Long = 0, isBlocked: Boolean = false)
    fun onCheckedPhone(isValid: Boolean)
    fun showCountDownRetrySms(countDown: Long)
}