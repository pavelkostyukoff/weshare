package com.spacesofting.weshare.mvp.view

import com.arellomobile.mvp.MvpView
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.Photo

interface ProfileEditView : MvpView {
    fun showProgress(b: Boolean)
    fun setPhotoForPicasso(photo: Photo)
    fun showNewInfo(newinfo : User)
}
