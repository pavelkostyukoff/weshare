package com.spacesofting.weshare.ui.fragment.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.pawegio.kandroid.runOnUiThread
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.ui.fragment.ImagePickerFragment
import com.spacesofting.weshare.ui.fragment.presentation.view.EditProfileView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

@InjectViewState
class EditProfilePresenter : MvpPresenter<EditProfileView>(), ImagePickerFragment.PickerListener {

    val PATTERN = Pattern.compile("[a-zA-Z0-9а-яА-Я_.$%*)(!@:|]{4,32}")
    val MAX_NICK_LINGTH = 32

    enum class Field {
        NICK,
        IMAGE
    }

    var profile: Profile? = null
    var profileNew: Profile = Profile()
    var imageFile: File? = null
    var timeout: Timer? = Timer()

    private var isNickNameValid = false
    private var isAddedNewAvatar = false
    private var isCheckAlreadyExist = false

    init {
        //todo запрос оправляется как только мы хотим показать профиль после логина
       /* Api.Users.get()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ profile ->
                this.profile = profile
                profileNew = profile.clone() as Profile
                viewState.showProfile(profile)
                viewState.setTitle(R.string.edit_profile_edit)
            }, { e ->
                profileNew = Profile()
                viewState.showProfile(profileNew)
                viewState.setTitle(R.string.edit_profile_create)
            })*/
    }

    fun onBackPressed() {
        // get difference
        val equals = profile?.equals(profileNew)

        //choose dialog to show
        if (equals != null && equals) {
            viewState.close()
        } else if (isValid()) {
            viewState.save()
        } else {
            viewState.cancel()
        }
    }

    fun save() {
        val imageSize = imageFile?.let {
            it.length() / (Settings.THE_SIZE_OF_A_MEGABYTE * Settings.THE_SIZE_OF_A_MEGABYTE)
        }
        viewState.showProgress()
        //save image
        imageFile?.let {
            imageSize?.let {
                var saveImgFile = imageFile!!

                if (imageSize > Settings.LIMIT_IMAGE_SIZE) {
                    saveImgFile = ImageUtils.compressPhoto(imageFile!!)
                }
                ImageUtils.send(saveImgFile)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe({ img ->
                        profileNew.img = img
                        updateProfile()
                    }, { e ->
                        viewState.saved(false)
                        viewState.showToast(R.string.error_link_image)
                    })
            }
        } ?: run {
            updateProfile()
        }
    }

    private fun updateProfile(){
        //update and profile
        val observable: Observable<Profile>
        var isNew = false

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
            })
    }

    fun fieldChanged(value: String?, field: Field) {
        if (field == Field.NICK) {
            isCheckAlreadyExist = false

            value?.let {
                if ((it.isNotEmpty() && it.length >= 3 && it != profile?.nick) || (it.isNotEmpty() && it.length >= 3 && profile == null)){
                    isCheckAlreadyExist = true
                    timeout?.cancel()
                    timeout = Timer()
                    timeout?.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                checkExistNick(it)
                                profileNew.nick = it
                            }
                        }
                    }, 500)
                } else {
                    timeout?.cancel()
                    viewState.saveButtonEnabled(false)
                    isNickNameValid = false
                    profileNew.nick = it

                    if(it.isEmpty()){
                        viewState.onCheckNick(EditProfileView.NONE)
                    } else if(it == profile?.nick){
                        isNickNameValid = true
                        viewState.onCheckNick(EditProfileView.NONE)
                        viewState.saveButtonEnabled(isAddedNewAvatar)
                    } else if(it.length < 3 && it.isNotEmpty()) {
                        viewState.onCheckNick(EditProfileView.SHORT_NICK)
                    } else if(it.length > MAX_NICK_LINGTH){
                        viewState.onCheckNick(EditProfileView.LONG_NICK)
                    }
                    return@let
                }
            }
        } else if (field == Field.IMAGE) {
            profileNew.img = Image(value)
            viewState.saveButtonEnabled(isValid())
        }
    }

    fun isValid(): Boolean {
        if(profileNew.nick != null) {
            return PATTERN.matcher(profileNew.nick).matches()
        }else{
            return false
        }
    }

    fun hasProfile(): Boolean {
        if(ApplicationWrapper.instance.profile != null) {
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
                ApplicationWrapper.instance.profile = profile
                viewState.showToast(R.string.error_link_image)
            })
    }

    override fun onPickerIncorrectUrl(exception: MalformedURLException) {
        viewState.showToast(R.string.image_url_malformed)
    }

    override fun onEditPhotoConfirmClick() {
        imageFile?.let {
            isAddedNewAvatar = true
            viewState.showImage(it)
            viewState.saveButtonEnabled(isNickNameValid)
        }
    }

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

        Api.Users.checkNickName(NickName(value))
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
            })
    }
}
