package com.spacesofting.weshare.mvp.presentation.presenter

import android.annotation.SuppressLint
import moxy.InjectViewState
import moxy.MvpPresenter
import com.pawegio.kandroid.runOnUiThread
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.BoomerangoRouter
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.UpdateProfile
import com.spacesofting.weshare.mvp.presentation.view.EditProfileView
import com.spacesofting.weshare.mvp.ui.fragment.ImagePickerFragment
import com.spacesofting.weshare.mvp.ui.fragment.InventoryFragment.Companion.SCANNER_REQUEST_CODE
import com.spacesofting.weshare.utils.ImageUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

@InjectViewState
class EditProfilePresenter : MvpPresenter<EditProfileView>(), ImagePickerFragment.PickerListener {

    val router: BoomerangoRouter = ApplicationWrapper.instance.getRouter()
    val PATTERN = Pattern.compile("[a-zA-Z0-9а-яА-Я_.$%*)(!@:|]{4,32}")
    val MAX_NICK_LINGTH = 32

    enum class Field {
        NICK,
        IMAGE
    }

    var profile: User? = null
    var profileNew: User = User()
    var imageFile: File? = null
    var timeout: Timer? = Timer()

    private var isNickNameValid = false
    private var isAddedNewAvatar = false
    private var isCheckAlreadyExist = false

    init {
        this.profile = Settings.get()//ApplicationWrapper.user
        profile?.let { viewState.showProfile(it) }
        //todo запрос оправляется как только мы хотим показать профиль после логина
        /* Api.Users.getAccount()
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({ profile ->
                 this.profile = profile
              //   profileNew = profile.clone() as Profile
                 viewState.showProfile(profile)
               //  viewState.setTitle(R.string.edit_profile_edit)
             }, { e ->
                 viewState.showProfile(profileNew)
             //    viewState.setTitle(R.string.edit_profile_create)
             })*/
    }

/*
    fun onBackPressed() {
        // get difference
        //  val equals = profile?.equals(profileNew)

        //choose dialog to show
        */
/* if (equals != null && equals) {
             viewState.close()
         } else if (isValid()) {
             viewState.save()
         } else {
             viewState.cancel()
         }*//*

    }
*/

    fun deletePhoto() {
        /*     Api.Pictures.delPicture()
                 .observeOn(AndroidSchedulers.mainThread())
                 .doFinally { viewState.showProgress(false) }
                 .subscribe ({
                     viewState.deletePhotos()

                 }){

                 }*/
    }

    private fun updateProfileAvatar(url: String?) {
        getMyProfile()
        //update and profile
        //todo все ясно как только ендпоинт заработает аватарка поставится
        /*  val observable: Observable<Photo>
          var isNew = false
          observable = Api.Users.update(profileNew)
          observable = Api.Users.updateAvatar()
          if (profile != null) {
              observable = Api.Users.update(profileNew)
          } else {
              isNew = true
              observable = Api.Users.make(profileNew)
          }

          observable.subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({
                      profile ->
                  ApplicationWrapper.instance.profile = profile
                  viewState.saved(true, isNew)
              }, {
                      e ->
                  ApplicationWrapper.instance.profile = profile
                  viewState.saved(false)
              })*/
    }

    fun fieldChanged(value: String?, field: Field) {
        if (field == Field.NICK) {
            isCheckAlreadyExist = false

            value?.let {
                if ((it.isNotEmpty() && it.length >= 3 && it != profile?.firstName) || (it.isNotEmpty() && it.length >= 3 && profile == null)) {
                    isCheckAlreadyExist = true
                    timeout?.cancel()
                    timeout = Timer()
                    timeout?.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                checkExistNick(it)
                                //   profileNew.firstName = it
                            }
                        }
                    }, 500)
                } else {
                    timeout?.cancel()
                    viewState.saveButtonEnabled(false)
                    isNickNameValid = false
                    //     profileNew.firstName = it

                    if (it.isEmpty()) {
                        viewState.onCheckNick(EditProfileView.NONE)
                    } else if (it == profile?.firstName) {
                        isNickNameValid = true
                        viewState.onCheckNick(EditProfileView.NONE)
                        viewState.saveButtonEnabled(isAddedNewAvatar)
                    } else if (it.length < 3 && it.isNotEmpty()) {
                        viewState.onCheckNick(EditProfileView.SHORT_NICK)
                    } else if (it.length > MAX_NICK_LINGTH) {
                        viewState.onCheckNick(EditProfileView.LONG_NICK)
                    }
                    return@let
                }
            }
        } else if (field == Field.IMAGE) {
            //  profileNew.img = Image(value)
            viewState.saveButtonEnabled(isValid())
        }
    }

    fun isValid(): Boolean {
        /*    if(profileNew.firstName != null) {
                return PATTERN.matcher(profileNew.firstName).matches()
            }else{
                return false
            }*/
        return false
    }

    fun hasProfile(): Boolean {
        if (Settings.get() != null) {
            return true
        } else {
            Settings.logout()
            return false
        }
    }

    override fun onPickerGalleryClick() {
        viewState.openGallery()
    }

    override fun onPickerCameraClick() {
        imageFile = ImageUtils.createImageFile() //savePhotoFile
        imageFile?.let {
            viewState.openCamera(it)
        }
    }

    @SuppressLint("CheckResult")
    override fun onPickerUrlConfirm(url: URL) {
        ImageUtils.download(url.toString())
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                imageFile = res
                runOnUiThread {
                    viewState.setPreviewPhoto(res)
                }
            }, { e ->
                //  ApplicationWrapper.instance.profile = User
                viewState.showToast(R.string.error_message)
            })
    }

    override fun onPickerIncorrectUrl(exception: MalformedURLException) {
        viewState.showToast(R.string.error_message)
    }

    /* fun saveAvatar()
     {
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
     }*/
    override fun onEditPhotoConfirmClick() {
        imageFile?.let {
            //  viewState.showWishImage(it)
            //   savePhoto()
            isAddedNewAvatar = true
            saveImageOrCompress()
        }
        /*imageFile?.let {

            Api.Pictures.sentAvatar(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                        response->{


                    response.url
                }
                },{
                        e->
                })
        }*/


        // viewState.saveButtonEnabled(isNickNameValid)


        /*imageFile?.let {
            Api.Users.updateAvatar(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile ->


                  //  this.profileNew = profile
                    //   profileNew = profile.clone() as Profile
                  //  router.exit()
                    viewState.showProfile(profileNew)


                    imageFile?.let {
                        isAddedNewAvatar = true
                        viewState.showImage(it)
                        viewState.saveButtonEnabled(isNickNameValid)
                    }
                    //  viewState.setTitle(R.string.edit_profile_edit)
                }, { e ->
                    //  profileNew = Profile()
                    viewState.showProfile(profileNew)
                    //    viewState.setTitle(R.string.edit_profile_create)
                })*/


    }

    /* override fun onPickerCameraClick() {
         imageFile = ImageUtils.savePhotoFile()
         viewState.openCamera(imageFile!!)
     }*/
    fun onCameraResult() {
        imageFile?.let {
            viewState.setPreviewPhoto(it)
        }
    }

    fun onGalleryResult(file: File) {
        imageFile = file
        viewState.setPreviewPhoto(file)
    }

    fun checkExistNick(value: String) {
        viewState.showCheckNickProgress(true)
        viewState.saveButtonEnabled(false)

        /*Api.Users.checkNickName(NickName(value))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ nickNameStatus ->
                if(isCheckAlreadyExist) {
                    isNickNameValid = (nickNameStatus.status == NickNameStatus.AVAILABLE)

                    viewState.saveButtonEnabled(isNickNameValid)
                    viewState.onCheckNick(nickNameStatus.status)
                }
                viewState.showCheckNickProgress(false)
            }, { error ->
                viewState.showCheckNickProgress(false)

                if ((error as HttpException).code() == 400 && isCheckAlreadyExist) {
                    isNickNameValid = false
                    viewState.onCheckNick(EditProfileView.ALREADY_EXIST)
                    viewState.saveButtonEnabled(false)
                }
            })*/
    }

    //todo вешать на кнопку сохранения передает на общий экран обратно данные профиля

    private fun getMyProfile() {
        with(Api) {
            Users.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile ->
                    Settings.set(profile)
                }, { e ->
                })
        }
    }

    fun changeMyProfile(updProfile: UpdateProfile) {
        viewState.showProgress(true)
        Api.Users.updateProfile(updProfile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe({
                Settings.set(it)
                // ApplicationWrapper.user = it
                //  viewState.showNewInfo(it)
                //todo проходим в основной экран
                //todo тут может отправить во вью и показать тост
                router?.exitWithResult(SCANNER_REQUEST_CODE, it)
            }) {
                it
                //   router?.exit()
                //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
            }
    }

    private fun saveImageOrCompress() {
        if (isAddedNewAvatar) {
            imageFile?.let {
                val imageSize =
                    it.length() / (Settings.THE_SIZE_OF_A_MEGABYTE * Settings.THE_SIZE_OF_A_MEGABYTE)
                var saveImgFile = it
                viewState.showProgress(true)
                if (imageSize >= Settings.LIMIT_IMAGE_SIZE) {
                    saveImgFile = ImageUtils.compressPhoto(it)
                }
                ImageUtils.sendAvatar(saveImgFile)?.subscribeOn(AndroidSchedulers.mainThread())
                    .doFinally { viewState.showProgress(false) }
                    ?.subscribe({
                            avatar ->
                        viewState.updateAvatar(avatar.url)
                        updateProfileAvatar(avatar.url)
                    }, { e ->
                        viewState.showToast(R.string.photo_server_full)
                        //    stopShowProgress()
                    })

                  /*  .subscribe({ img ->
                        viewState.showProgress(false)
                    }, { e ->
                        viewState.showProgress(false)
                    })
                    .dispose()*/
            }
        }
    }
}
