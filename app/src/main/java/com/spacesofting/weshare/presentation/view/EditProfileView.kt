package com.spacesofting.weshare.presentation.view

import com.spacesofting.weshare.data.api.NickNameStatus
import com.spacesofting.weshare.presentation.mvp.User
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.io.File
@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface EditProfileView : MvpView {

 //   fun showNewInfo(newinfo : User)

    companion object {
        val AVAILABLE       = NickNameStatus.AVAILABLE
        val ALREADY_EXIST   = NickNameStatus.BLOCKED
        val LONG_NICK       = 2
        val SHORT_NICK      = 3
        val NONE            = 4
    }

    fun showProgress(isInProgress: Boolean = true)
    fun showProfile(profile: User)
    fun saved(isSuccess: Boolean, isNew: Boolean = false)
    fun showToast(stringId: Int)
    fun updateAvatar(img: String?)
    fun deletePhotos()

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
