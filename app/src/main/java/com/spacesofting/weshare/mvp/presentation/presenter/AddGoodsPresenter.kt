package com.spacesofting.weshare.mvp.presentation.presenter

import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.Entity
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.model.Advert
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
class AddGoodsPresenter : MvpPresenter<AddGoodsView>(), ImagePickerFragment.PickerListener {

    var imageFile: File? = null
    var imageChanged = false
    var timeout: Timer? = null
    val ITEMS_PER_PAGE = 15
    val ITEMS_PER_PAGE_WISH_LIST = 5
    var page = 0
    var advertOld = ApplicationWrapper.advert

    var newAdvert = Advert()
    val nameMaxLength = 48
    val nameMinLength = 3
    val descriptionMaxLength = 800
    val descriptionMinLength = 10
    var advert: Advert? = null

    var isSharingController: Boolean = false
    var isWishImageEdit: Boolean = false
    var isWishFieldsEdit: Boolean = false
    var editWishName: String? = null
    var editWishDescription: String? = null
    var goodId: String = ""
    var goodIdForPatch: String = ""
    private var goodImageId: String = ""
    var editWishAmount: String? = null
    var editWishImage: String? = null
    var addressIsNotNill: String? = null
    var priceIsNotNill: String? = null

    //todo тут ответ из пикер - ок поехали дальше закрываем пикер и показываем
    override fun onEditPhotoConfirmClick() {
        imageFile?.let {
            //  viewState.showWishImage(it)
            // savePhoto()
            imageChanged = true
            saveImageOrCompress()
        }
    }

    enum class Field {
        IMAGE,
        NAME,
        DESCRIPTION,
        PRICE,
        PRIVACY,
        ADRESS,
        WISH_URL
    }

    private fun publishAdvert(goodId: String) {
        with(Api) {
            with(Adverts) {
                    publishMyAdvert(goodId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                                advert ->

                            viewState.saved(advert)

                            viewState.showProgress(false)
                            //checkExternalApp()

                        }, { error ->
                            viewState.showProgress(false)
                          //  viewState.saved(false)
                            viewState.showToast(R.string.error_general)
                        })

            }
        }
    }

    private fun saveAdvert(advert: Advert) {
        ApplicationWrapper.instance.setAuthorityWish(advert)
        //todo -   /me/adverts/{advertId}/publish
        //add or update wish
        // if (advert.id != 0) {
     /*   goodId
        //todo тут - /me/adverts/{advertId}
        goodId = ApplicationWrapper.instance.getAuthorityWish().toString()*/
                goodId.let {
                    Api.Adverts.updateMyAdvertById(advert,it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ advert ->
                          //  viewState.saved(true)
                            viewState.showProgress(false)
                            //checkExternalApp()
                            publishAdvert(goodId)

                        }, { error ->
                            viewState.showProgress(false)
                           // viewState.saved(false)
                            viewState.showToast(R.string.error_general)
                        })
        }
        advert.let { ApplicationWrapper.instance.setAuthorityWish(null, null) }

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
        var isValid = false
        //   Field.PRIVACY       -> newWish.privacy = privacy
        when (field) {
            Field.IMAGE -> imageChanged = true
            Field.DESCRIPTION -> newAdvert.description = value
            Field.NAME -> newAdvert.title = value
            Field.ADRESS -> addressIsNotNill = value.toString()
            Field.PRICE -> {
                priceIsNotNill = value.toString()
                //todo заполнеить вместо числа
                //todo из объекта в котором сумма Double период и валюта
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
            //    advertOld.title = newAdvert.title
        }

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
        if (newAdvert.title == null) {
            //   newAdvert.rentPeriods = price
            viewState.emptyTitle(true)
        } else {
            // advert?.rentPeriods?.get(0)?.amount = price// RentPeriod(price)
            isValid = isValid(newAdvert)
            viewState.emptyTitle(false)
        }

        if (newAdvert.description == null) {
            //   newAdvert.rentPeriods = price
            viewState.emptyDesc(true)
        } else {
            // advert?.rentPeriods?.get(0)?.amount = price// RentPeriod(price)
            isValid = isValid(newAdvert)
            viewState.emptyDesc(false)
        }


        if (priceIsNotNill == null || addressIsNotNill == null) {
            //   newAdvert.rentPeriods = price
            viewState.emptyPrice(true)
        } else {
            // advert?.rentPeriods?.get(0)?.amount = price// RentPeriod(price)
            isValid = isValid(newAdvert)
            viewState.emptyPrice(false)
        }

        viewState.setConfirmButtonState(isValid)
    }

    private fun confirmPressed(advert: Advert) {
        if (isValid(advert)) {
            saveAdvert(advert)
        } else {
            onBackPressed()
        }
    }

    private fun saveImageOrCompress() {
        //todo тут - /me/adverts/{advertId}/images
        if (imageChanged) {
            imageFile?.let {
                val imageSize =
                    it.length() / (Settings.THE_SIZE_OF_A_MEGABYTE * Settings.THE_SIZE_OF_A_MEGABYTE)
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

                /*    ImageUtils.send(saveImgFile,goodId)
                    ?.subscribeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ img ->*/
                if (imageSize > Settings.LIMIT_IMAGE_SIZE) {
                    saveImgFile = ImageUtils.compressPhoto(it)
                }
                ImageUtils.send(saveImgFile, goodId)
                    ?.subscribeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                            img ->
                        viewState.showProgress(false)
                        viewState.showWishImage(it)
                      //  saveWish()

                    }, { e ->
                        e
                        //    stopShowProgress()
                    })


            }}

    }

    fun checkEditFieldsOrImage(advert: Advert) {
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

        confirmPressed(advert)
    }

    fun isValid(advert: Advert): Boolean {
        val title        = advert.title
        val description = advert.description
        val price       = advert.rentPeriods
        val address       = advert.address?.address

        /*val url         = advert.url*/

       // viewState.showYMButton()
/*
        if (!isUrlValidate(url)){
            return false
        }*/
        /*else*/ if (title == null || title.isEmpty() || title.length <= nameMinLength || title.length > nameMaxLength) {
            viewState.emptyTitle(true)
            return false
        }
       /* else if (address == null || advert.address?.address!!.isEmpty() *//*|| advert.address?.address!!.length > 5*//*) {
            return false
        }*/
        else if (description == null || description.isEmpty() || description.length <= descriptionMinLength || description.length > descriptionMaxLength) {
            viewState.emptyDesc(true)
            return false
        }
      /*  else if(price == null){
            return false
        }*/

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
        Api.Pictures.delPictureMyGood(goodId!!, goodImageId)
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

    fun getSubCategory(ent: Entity?) {
        with(Api) {
            Tags.getCategories(ent?.id, ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.entities?.isNotEmpty()!!)
                    {
                        val arr = ArrayList<Entity>()
                        ent?.name = "Все"
                        ent?.let { it1 -> arr.add(it1) }
                        it.entities
                        it.entities?.let { it1 -> arr.addAll(it1) }
                        it.entities = arr
                    }
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
