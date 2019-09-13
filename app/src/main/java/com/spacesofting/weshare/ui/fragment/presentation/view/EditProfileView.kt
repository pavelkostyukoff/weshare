package com.spacesofting.weshare.ui.fragment.presentation.view

import com.arellomobile.mvp.MvpView
import com.spacesofting.weshare.api.NickNameStatus
import com.spacesofting.weshare.mvp.Profile
import java.io.File

interface EditProfileView : MvpView {
    companion object {
        val AVAILABLE       = NickNameStatus.AVAILABLE
        val ALREADY_EXIST   = NickNameStatus.BLOCKED
        val LONG_NICK       = 2
        val SHORT_NICK      = 3
        val NONE            = 4
    }

    fun showProgress(isInProgress: Boolean = true)
    fun showProfile(profile: Profile)
    fun saved(isSuccess: Boolean, isNew: Boolean = false)
    fun showToast(stringId: Int)

    fun save()
    fun cancel()
    fun close()

    fun openGallery()
    fun openCamera(file : File)

    fun showImage(file: File)

    fun setPreviewPhoto(file: File)

    fun onCheckNick(status: Int)
    fun showCheckNickProgress(showing: Boolean)
    fun saveButtonEnabled(isEnabled: Boolean)
    fun setTitle(resString: Int)
}
