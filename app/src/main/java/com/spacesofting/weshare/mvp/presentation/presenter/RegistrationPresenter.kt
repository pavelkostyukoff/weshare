package com.spacesofting.weshare.mvp.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import com.pawegio.kandroid.d
import com.pawegio.kandroid.runOnUiThread
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.EnumErrorType
import com.spacesofting.weshare.api.HttpStatusCode
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Login
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.Mail
import com.spacesofting.weshare.mvp.model.MailComfirm
import com.spacesofting.weshare.mvp.presentation.view.RegistrationView
import com.spacesofting.weshare.mvp.ui.fragment.ImagePickerFragment
import com.spacesofting.weshare.utils.ErrorUtils
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.ServerException
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

@InjectViewState
class RegistrationPresenter : MvpPresenter<RegistrationView>(), ImagePickerFragment.PickerListener {

    val router = ApplicationWrapper.instance.getRouter()
    val PATTERN = Pattern.compile("[a-zA-Z0-9а-яА-Я_.$%*)(!@:|]{4,32}")
    val MAX_NICK_LINGTH = 32

    val login = Login()
    private val mail = Mail()

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

    fun deletePhoto() {
      /*  Api.Pictures.delPicture()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe({
                viewState.deletePhotos()

            }) {

            }*/
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
              /*  ImageUtils.send(saveImgFile, goodId)?.subscribeOn(AndroidSchedulers.mainThread())
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
                    })*/
            }
        } ?: run {
            updateProfile()
        }
    }

    private fun updateProfile() {
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
                        viewState.onCheckNick(RegistrationView.NONE)
                    } else if (it == profile?.firstName) {
                        isNickNameValid = true
                        viewState.onCheckNick(RegistrationView.NONE)
                        viewState.saveButtonEnabled(isAddedNewAvatar)
                    } else if (it.length < 3 && it.isNotEmpty()) {
                        viewState.onCheckNick(RegistrationView.SHORT_NICK)
                    } else if (it.length > MAX_NICK_LINGTH) {
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
                //  ApplicationWrapper.instance.profile = User
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
        mail.login = profile.login
        login.login = profile.login
        login.password = profile.password
        Api.Users.register(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe({
                    it
                viewState.showEmailComfirmDialog(profile.login)
                /*    when (it.code()) {
                    HttpStatusCode.NO_CONTENT,
                    HttpStatusCode.OK -> {

                    }
                    HttpStatusCode.NOT_FOUND -> {

                    }
                }*/

            }) {
                it
                val error = ErrorUtils.parseError(it)
                parseError(error)
                /*//todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                if (it.message.equals("User already exists"))
                {
                    //повторный запрос
                }
                else {
                    it.message?.let { it1 -> viewState.toastError(it1) }
                    router.exit()
                }*/
                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
            }
    }

    private fun parseError(error: ServerException) {
        when (error.type) {
            EnumErrorType.REQUIRED_CONFIRMATION -> {
                //  val timeout = TimerUtils().timeToMillis(error.extraFields?.timeout?.elapsed)
               //Settings.refreshToken = error.extraFields?.validationToken
                //  router.navigateTo(ScreenPool.SMS_CONFIRMATION_FRAGMENT, SmsRegistration(registration, timeout))
            }
            EnumErrorType.MODEL_VALIDATION -> {
                d("ERROR_TYPE", "validation model")
            }
            EnumErrorType.TIMEOUT -> {
                //   val timeout = TimerUtils().timeToMillis(error.extraFields?.timeout?.elapsed)
                //   viewState.showErrorMessage(true, R.string.auth_user_blocked, timeout, true)
            }
            EnumErrorType.ERRORS -> {
                val reasons = error.extraFields?.errors?.get(0)?.reasons?.get(0)
                viewState.toastError(error.message)
                when(error.message)
                {
                    "Too many request to verify email. Try again later."->{
                        viewState.errorDlg("Слишком много запросов проверки mail, попробуйте позже")
                    }
                    "User already exists"-> {

                        verifyMailRequest(mail)
                    }
                    "User not found or alerady verified" -> {
                        viewState.errorDlg("Пользователь с такой почтой уже был зарегистрирован")
                    }
                }
                /*      when(reasons) {
                 //   EnumReasons.WRONG_SMS_CODE -> {
                //        viewState.showErrorMessage(true, R.string.auth_wrong_sms_code)
                    }

                    else -> {
                  //      viewState.showErrorMessage(true, R.string.error_general)
                    }*/
            }
        }
    }

    private fun verifyMailRequest(mail: Mail) {
        Api.Users.verifyMailRequest(mail)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe({

                when (it.code()) {
                    HttpStatusCode.NO_CONTENT,
                    HttpStatusCode.CREATED,
                    HttpStatusCode.OK -> {
                        viewState.showEmailComfirmDialog(mail.login)
                    }
                    HttpStatusCode.NOT_FOUND -> {
                        viewState.errorDlg("Такой пользователь уже зарегестирован или не найден в системе")
                       // viewState.toastError("Такой пользователь уже зарегестирован или не найден в системе")
                    }
                }
                //todo проходим в основной экран
                //todo проходим в основной экран
                //todo тут может отправить во вью и показать тост
                //        router?.exitWithResult(SCANNER_REQUEST_CODE, it)
            }) {
                val error = ErrorUtils.parseError(it)
                parseError(error)
             //   router?.exit()
                //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
               // viewState.showToast(R.string.reg_filed)
            }
    }


    fun comfirmMail(text: MailComfirm?) {
            Api.Users.comfirmMeil(text)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { viewState.showProgress(false) }
                .subscribe({
                //   viewState.showEmailComfirmDialog(profile?.login)

                    autorize()
                    //todo проходим в основной экран
                    //todo тут может отправить во вью и показать тост
                    //        router?.exitWithResult(SCANNER_REQUEST_CODE, it)
                }) {
                    router?.exit()
                    //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                    //router.navigateTo(ScreenPool.BASE_FRAGMENT)
                    viewState.showToast(R.string.reg_filed)
                }
        }

         fun autorize() {
            if (login != null) {
                viewState.showProgress(true)
                Api.Auth.autorize(login)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { viewState.showProgress(false) }
                    .subscribe({ autorize ->
                        Settings.AccessToken = autorize.accessToken
                        Settings.refreshToken = autorize.rowrefreshTokenVersion
                        router.navigateTo(ScreenPool.FEED_FRAGMENT)
                        viewState.showToast(R.string.reg_succesfull)
                        //todo тут кладем токен в сохранялки Settings
                    }) {
                        it.message?.let { it1 -> viewState.toastError(it1) }
                    }
            }
        }

    }