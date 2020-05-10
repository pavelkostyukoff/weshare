package com.spacesofting.weshare.mvp.presentation.presenter

import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.mvp.presentation.view.AddGoodsView
import com.spacesofting.weshare.mvp.ui.fragment.ImagePickerFragment
import com.spacesofting.weshare.utils.ImageUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*

@InjectViewState
class AddGoodsPresenter : MvpPresenter<AddGoodsView>() , ImagePickerFragment.PickerListener {

    var imageFile: File?            = null
    var imageChanged                = false
    var timeout: Timer?             = null
    val ITEMS_PER_PAGE = 15
    val ITEMS_PER_PAGE_WISH_LIST = 5
    var page = 0

    var isSharingController: Boolean    = false
    var isWishImageEdit: Boolean        = false
    var isWishFieldsEdit: Boolean       = false
    var editWishName: String?           = null
    var editWishDescription: String?    = null
    var goodId: String    = ""
    private var goodImageId: String    = ""
    var editWishAmount: String?         = null
    var editWishImage: String?          = null

//todo тут ответ из пикер - ок поехали дальше закрываем пикер и показываем
    override fun onEditPhotoConfirmClick() {
        imageFile?.let {
            //  viewState.showWishImage(it)
                viewState.showWishImage(it)
            savePhoto()
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

    //todo после получения с камеры или с галереи есть колбек в котором мы обновляем переменную с рисунком фалом и далее ее юзаем
    fun onCameraResult() {
        imageFile?.let {

            //todo  или успех или ошибка
            viewState.setPreviewImg(it)
        }
    }

    fun delPictureMyGood () {
        Api.Pictures.delPictureMyGood(goodId,goodImageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it
                    viewState.wishImageDelete()
                    viewState.showToast(R.string.imag_load_ok_delete)
                //todo  показать тост
                //todo загрузить заглушку в пикассо
            })  {
                viewState.showToast(R.string.error_load_ok_delete)
                //todo  показать тост о неудаче
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

    fun getSubCategory(id: String?) {
        with(Api) {
            Tags.getListCompilations(id ,ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    viewState.setNewSubCategory(it)

                })  {
                    viewState.showToast(R.string.error_load_ok_delete)
                }
        }
    }


    fun savePhoto() {
        viewState.showProgress(true)
        imageFile?.let { photoFile ->
            ImageUtils.send(photoFile, goodId)
                ?.subscribeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ photo ->
                    viewState.showProgress(false)
                    viewState.showToast(R.string.imag_load_ok_save)
                }, {
                    viewState.showProgress(false)
                    viewState.showToast(R.string.error_load_image)
                })
        }
    }
}
