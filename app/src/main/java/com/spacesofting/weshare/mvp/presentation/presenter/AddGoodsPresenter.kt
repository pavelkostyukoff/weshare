package com.spacesofting.weshare.mvp.presentation.presenter

import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.HttpStatusCode
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.model.Advert
import com.spacesofting.weshare.mvp.presentation.view.AddGoodsView
import com.spacesofting.weshare.mvp.ui.fragment.ImagePickerFragment
import com.spacesofting.weshare.utils.ImageUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.HttpException
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*

@InjectViewState
class AddGoodsPresenter : MvpPresenter<AddGoodsView>(), ImagePickerFragment.PickerListener {

    var imageFile: File? = null
    var imageChanged = false
    var timeout: Timer? = null
    val ITEMS_PER_PAGE = 15
    val ITEMS_PER_PAGE_WISH_LIST = 5
    var page = 0

    var newAdvert                 = Advert()
    val nameMaxLength           = 48
    val descriptionMaxLength    = 800
    var advert: Advert?             = null

    var isSharingController: Boolean = false
    var isWishImageEdit: Boolean = false
    var isWishFieldsEdit: Boolean = false
    var editWishName: String? = null
    var editWishDescription: String? = null
    var goodId: String = ""
    private var goodImageId: String = ""
    var editWishAmount: String? = null
    var editWishImage: String? = null

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

    private fun saveWish() {
      //todo -   /me/adverts/{advertId}/publish
        //add or update wish
       // if (advert.id != 0) {
        //todo тут - /me/adverts/{advertId}
       /*     Api.Wishes.wishEdit(newWish.id, newWish)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wish ->
                    viewState.saved(true)
                    viewState.showProgress(false)
                    checkExternalApp()

                }, { error ->
                    viewState.showProgress(false)
                    viewState.saved(false)
                    viewState.showToast(R.string.error_general)
                })*/
      //  }
    }

    fun isWishEdit(): Boolean {
        var result = true
        advert?.let {
            if (it == newAdvert) {
                result = false
            }
        }

        return result
    }

    fun isNewWish(): Boolean {
        return advert == null
    }

    fun onBackPressed() {
        if (isValid(newAdvert)) {
            viewState.save()
        } else {
            viewState.cancel()
        }
    }

    //todo постоянная проверка валидации
    fun fieldChanged(value: String?, field: Field) {
        var isUrlChanged = false

        when (field) {
            Field.IMAGE         -> imageChanged = true
            Field.DESCRIPTION   -> newAdvert.description = value
            Field.NAME          -> newAdvert.title = value
         //   Field.PRIVACY       -> newWish.privacy = privacy
            Field.PRICE         -> {
                val price = value?.toDoubleOrNull()
                //todo заполнеить вместо числа
                //todo из объекта в котором сумма Double период и валюта
                if (price == null) {
                    newAdvert.rentPeriods = null
                    viewState.emptyPrice(true)
                } else {
                //    newAdvert.rentPeriods = RentPeriod(price)
                    viewState.emptyPrice(false)
                }
            }
           /* Field.WISH_URL -> {
                isUrlChanged = newAdvert.url != value
                newAdvert.url = value

                if (!value.isNullOrEmpty()) {
                    viewState.showClearUrlBtn(true)
                } else {
                    viewState.showClearUrlBtn(false)
                    timeout?.cancel()
                }
            }*/
        }

        val isValid = isValid(newAdvert)
    /*    if (!newWish.url.isNullOrEmpty() && field == Field.WISH_URL && isUrlValidate(value) && isWishEdit() && isUrlChanged) {
            timeout?.cancel()
            timeout = Timer()
            timeout?.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        getWishFromUrl(newWish.url!!)
                    }
                }
            }, 500)
        }*/

        viewState.setConfirmButtonState(isValid)
    }
    private fun confirmPressed() {
        if (isValid(newAdvert)) {
            saveImageOrCompress()
        } else {
            onBackPressed()
        }
    }

    private fun saveImageOrCompress() {
        //todo тут - /me/adverts/{advertId}/images
        if (imageChanged) {
            imageFile?.let {
                val imageSize = it.length() / (Settings.THE_SIZE_OF_A_MEGABYTE * Settings.THE_SIZE_OF_A_MEGABYTE)
                var saveImgFile = it
/*todo реализовать сценарий если не авторизован - переход к авторизации !!!
                if(!Settings.isAuthenticated() && !Settings.isScenarioFinished && Settings.scenario == ScenarioActivity.SCENARIO_THIRD){
                    ApplicationWrapper.instance.setAuthorityWish(newWish, it)
                    viewState.goToRegisterFragment()
                    return
                } else if (!Settings.isAuthenticated()) {
                    viewState.goToRegistration()
                }*/

                viewState.showProgress(true)

                ImageUtils.send(saveImgFile,goodId)
                    ?.subscribeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ img ->
                        saveWish()
                    }, { error ->
                        if ((error as? HttpException)?.code() == HttpStatusCode.UNAUTHORIZED) {
                            if (imageSize > Settings.LIMIT_IMAGE_SIZE) {
                                saveImgFile = ImageUtils.compressPhoto(it)
                                ImageUtils.send(saveImgFile,goodId)
                                    ?.subscribeOn(AndroidSchedulers.mainThread())
                                    ?.subscribe({ img ->
                                        viewState.showProgress(false)
                                        saveWish()
                                    }, { e ->
                                    //    stopShowProgress()
                                    })
                            } else {
                              //  stopShowProgress()
                            }
                        } else {
                          //  stopShowProgress()
                        }
                    })
            }
        } else {
         //todo   saveWish(null)
        }
    }

    fun checkEditFieldsOrImage() {
        //check edit fields for appsflyer
      /*  if (newAdvert.rentPeriods?.amount.toString() != editWishAmount
            || newAdvert.title.toString() != editWishName
            || newAdvert.description.toString() != editWishDescription) {
            isWishFieldsEdit = true
        }*/

        //check edit image for appsflyer
        if (imageFile?.name != editWishImage) {
            isWishImageEdit = true
        }

        confirmPressed()
    }

    fun isValid(advert: Advert = newAdvert): Boolean {
        val name        = advert.title
        val description = advert.description
        val price       = advert.rentPeriods
        /*val url         = advert.url*/

       // viewState.showYMButton()
/*
        if (!isUrlValidate(url)){
            return false
        }*/
        /*else*/ if (name == null || name.isEmpty() || name.length > nameMaxLength) {
            return false
        }
        else if (description != null && description.length > descriptionMaxLength) {
            return false
        }
        else if(price == null){
            return false
        }

        return true
    }

    private fun loadImageFromUrl(url: URL) {
        with(ImageUtils) {
            download(url.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->
                    imageFile = res
                    viewState.setPreviewImg(res)
                }, {
                    viewState.showToast(R.string.error_general)
                })
        }
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

    fun delPictureMyGood() {
        Api.Pictures.delPictureMyGood(goodId, goodImageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it
                viewState.wishImageDelete()
                viewState.showToast(R.string.imag_load_ok_delete)
                //todo  показать тост
                //todo загрузить заглушку в пикассо
            }) {
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
            Tags.getListCompilations(id, ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.setNewSubCategory(it)
                }) {
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
