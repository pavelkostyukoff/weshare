package com.spacesofting.weshare.ui.fragment.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.pawegio.kandroid.runOnUiThread
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.MailComfirm
import com.spacesofting.weshare.ui.fragment.ImagePickerFragment
import com.spacesofting.weshare.ui.fragment.presentation.view.RegistrationView
import com.spacesofting.weshare.utils.ImageUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

@InjectViewState
class RegistrationPresenter : MvpPresenter<RegistrationView>(), ImagePickerFragment.PickerListener {

    val router = ApplicationWrapper.INSTANCE.getRouter()
    val PATTERN = Pattern.compile("[a-zA-Z0-9а-яА-Я_.$%*)(!@:|]{4,32}")
    val MAX_NICK_LINGTH = 32

    enum class Field {
        NICK,
        IMAGE
    }

    var profile: Profile? = null
    var profileNew: User = User()
    var imageFile: File? = null
    var timeout: Timer? = Timer()

    private var isNickNameValid = false
    private var isAddedNewAvatar = false
    private var isCheckAlreadyExist = false

    init {
     //   this.profile = ApplicationWrapper.user
      //  ApplicationWrapper.user?.let { viewState.showProfile(it) }

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


   /* fun showAvatar()
    {
        Api.Users.getAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ profile ->
                this.profile = profile
                //   profileNew = profile.clone() as Profile
                viewState.showProfile(profile)
                //  viewState.setTitle(R.string.edit_profile_edit)
            }, { e ->
                //  profileNew = Profile()
                viewState.showProfile(profileNew)
                //    viewState.setTitle(R.string.edit_profile_create)
            })
    }*/

    fun onBackPressed() {
        // get difference
      //  val equals = profile?.equals(profileNew)

        //choose dialog to show
       /* if (equals != null && equals) {
            viewState.close()
        } else if (isValid()) {
            viewState.save()
        } else {
            viewState.cancel()
        }*/
    }
/*    fun showComfirmEmailDialog () {
        viewState.showEmailComfirmDialog(profile.login)
    }*/

    fun deletePhoto()
    {
        Api.Pictures.delPicture()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
                viewState.deletePhotos()

            }){

            }
    }

    fun saveAvatar() {
        val imageSize = imageFile?.let {
            it.length() / (Settings.THE_SIZE_OF_A_MEGABYTE * Settings.THE_SIZE_OF_A_MEGABYTE)
        }
        viewState.showProgress(true)
        //save image
        imageFile?.let {
            imageSize?.let {
                var saveImgFile = imageFile!!

                if (imageSize > Settings.LIMIT_IMAGE_SIZE) {
                    saveImgFile = ImageUtils.compressPhoto(imageFile!!)
                }
                ImageUtils.send(saveImgFile)?.subscribeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ img ->
                        //profileNew.img = img
                       // this.profile?.let { it1 -> viewState.showProfile(it1) }
                        //this.profile
                        viewState.updateAvatar(img)
                        viewState.showProgress(false)

                        //   updateProfile()
                    }, { e ->
                        viewState.saved(false)
                       // viewState.showToast(R.string.error_link_image)
                    })
            }
        } ?: run {
            updateProfile()
        }
    }

    private fun updateProfile(){
    }

    fun fieldChanged(value: String?, field: Field) {
        if (field == Field.NICK) {
            isCheckAlreadyExist = false

            value?.let {
                if ((it.isNotEmpty() && it.length >= 3 && it != profile?.firstName) || (it.isNotEmpty() && it.length >= 3 && profile == null)){
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

                    if(it.isEmpty()){
                        viewState.onCheckNick(RegistrationView.NONE)
                    } else if(it == profile?.firstName){
                        isNickNameValid = true
                        viewState.onCheckNick(RegistrationView.NONE)
                        viewState.saveButtonEnabled(isAddedNewAvatar)
                    } else if(it.length < 3 && it.isNotEmpty()) {
                        viewState.onCheckNick(RegistrationView.SHORT_NICK)
                    } else if(it.length > MAX_NICK_LINGTH){
                        viewState.onCheckNick(RegistrationView.LONG_NICK)
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
        if(ApplicationWrapper.INSTANCE?.profile != null) {
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
        imageFile = ImageUtils.savePhotoFile()
        viewState.openCamera(imageFile!!)
    }

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
              //  ApplicationWrapper.INSTANCE.profile = User
                viewState.showToast(R.string.error_message)
            })
    }

    override fun onPickerIncorrectUrl(exception: MalformedURLException) {
        viewState.showToast(R.string.error_message)
    }

    override fun onEditPhotoConfirmClick() {

                        saveAvatar()
      /*  imageFile?.let {
            isAddedNewAvatar = true
            viewState.showImage(it)
            viewState.saveButtonEnabled(isNickNameValid)
        }*/

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
                    viewState.onCheckNick(RegistrationView.ALREADY_EXIST)
                    viewState.saveButtonEnabled(false)
                }
            })*/
    }
//todo вешать на кнопку сохранения передает на общий экран обратно данные профиля
    fun setRegistration(profile: Profile) {
        viewState.showProgress(true)

        Api.Users.register(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
                viewState.showEmailComfirmDialog(profile.login)

            }){
                it
                router?.exit()
                //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                viewState.toastError("Такой уже зарегистрирован или что-то иное надо настроить ошибки от сервера")

                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
            }

    }

    fun comfirmMail(text: MailComfirm?) {
        Api.Users.comfirmMeil(text)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
               // viewState.showEmailComfirmDialog(profile?.login)
                router.navigateTo(ScreenPool.AUTORIZE_FRAGMENT)
                viewState.showToast(R.string.reg_succesfull)

                //todo проходим в основной экран
                //todo тут может отправить во вью и показать тост
                //        router?.exitWithResult(SCANNER_REQUEST_CODE, it)
            }){
                router?.exit()
                //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
                viewState.showToast(R.string.reg_filed)

            }


    }
}
