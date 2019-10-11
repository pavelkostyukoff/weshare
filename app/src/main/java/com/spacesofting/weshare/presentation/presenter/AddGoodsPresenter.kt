package com.spacesofting.weshare.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.view.AddGoodsView
import com.spacesofting.weshare.ui.fragment.ImagePickerFragment
import com.spacesofting.weshare.utils.ImageUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*

@InjectViewState
class AddGoodsPresenter : MvpPresenter<AddGoodsView>() , ImagePickerFragment.PickerListener {

    var imageFile: File?            = null
    var imageChanged                = false
    var timeout: Timer?             = null

    var isSharingController: Boolean    = false
    var isWishImageEdit: Boolean        = false
    var isWishFieldsEdit: Boolean       = false
    var editWishName: String?           = null
    var editWishDescription: String?    = null
    var editWishAmount: String?         = null
    var editWishImage: String?          = null


    override fun onEditPhotoConfirmClick() {
        imageFile?.let {
            viewState.showWishImage(it)
        }
    }
    enum class Field {
        IMAGE,
        NAME,
        DESCRIPTION,
        PRICE,
        PRIVACY,
        WISH_URL
    }
    fun loadImageFromUrl(url: URL){
        ImageUtils.download(url.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                imageFile = res
                viewState.setPreviewImg(res)
            }, {
                viewState.showToast(R.string.error_general)
            })
    }

    override fun onPickerUrlConfirm(url: URL) {
        loadImageFromUrl(url)
    }

    override fun onPickerIncorrectUrl(exception: MalformedURLException) {
        viewState.showToast(R.string.edit_guest_card_label_first_name)
    }

    override fun onPickerGalleryClick() {
        viewState.openGallery()
    }

    fun onCameraResult() {
        imageFile?.let {
            viewState.setPreviewImg(it)
        }
    }
    fun onGalleryResult(file: File) {
        imageFile = file
        viewState.setPreviewImg(file)
    }

    override fun onPickerCameraClick() {
        imageFile = ImageUtils.savePhotoFile()
        imageFile?.let {
            viewState.openCamera(it)
        }
    }

}
