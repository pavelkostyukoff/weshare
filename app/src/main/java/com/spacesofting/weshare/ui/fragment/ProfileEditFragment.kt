package com.spacesofting.weshare.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.view.ProfileEditView
import com.spacesofting.weshare.mvp.presentation.ProfileEditPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Refrash
import com.spacesofting.weshare.mvp.UpdateProfile
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.Photo
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import java.io.File

class ProfileEditFragment : FragmentWrapper(), ProfileEditView {

    override fun showNewInfo(newinfo: User) {
        phone.setText(newinfo.phone.toString(), TextView.BufferType.EDITABLE)
        firstName.setText(newinfo.firstName.toString(), TextView.BufferType.EDITABLE)
        lastName.setText(newinfo.lastName.toString(), TextView.BufferType.EDITABLE)
        birthday.setText(newinfo.birthday.toString(), TextView.BufferType.EDITABLE)
    }

    override fun setPhotoForPicasso(photo: Photo) {
        Picasso.with(context)
            .load(photo.pictureName)
            //.resize(guestCard.photo!!.width, guestCard.photo!!.height)
            .into(myAvatar)
    }

    val user = ApplicationWrapper.user
    override fun showProgress(b: Boolean) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFragmentLayout(): Int {
return R.layout.fragment_profile_edit
    }

    companion object {
        const val TAG = "ProfileEditFragment"
        const val SCANNER_REQUEST_CODE = 100

        fun newInstance(): ProfileEditFragment {
            val fragment: ProfileEditFragment = ProfileEditFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ProfileEditPresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_INDICATOR_BACK_ARROW)
        goNewProfile.setOnClickListener {
            //  getMyEdit()
            val updProfile = UpdateProfile()
            updProfile.phone = phone.text.toString()
            updProfile.firstName = firstName.text.toString()
            updProfile.lastName = lastName.text.toString()
            updProfile.birthday = birthday.text.toString()
            presenter.chengeMyProfile(updProfile)//todo класс что кладем)
        }
        //todo жмем на карандаж запрашиваем права - если они есть
        editPhoto.setOnClickListener { checkPermission() }
        refresh.setOnClickListener { refreshed() }

        goNewProfile2.setOnClickListener {
            presenter.savePhoto()
        }
      /*  editPhoto.setOnClickListener {
            mPresenter

            //todo а в презентере viewState который перешлет фото сюда и поменяет его через пикассо
        }*/
    }
    fun refreshed()
    {
        val validationToken = Settings.ValidationToken
        //todo autorize
        val token = validationToken?.let { Refrash(it) }
        token?.let {
            Api.Auth.getNewToken(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ it->
                    Settings.AccessToken = it.token
                    Settings.ValidationToken = it.rowrefreshTokenVersion
                    ApplicationWrapper.user = it.user!!
                    //todo тут кладем токен в сохранялки Settings

                }){
                    it
                }
        }
    }


    private fun checkPermission() {
        activity?.let {
            RxPermissions(it)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isGranted ->
                    if (isGranted) {
                        //todo если права даны то открываем галерею
                        openGallery()
                    } else {
                        Toast.makeText(activity, R.string.permission_read_external_storage, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun openGallery() {
       // EasyImage.openGallery(this, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

     /*   EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                presenter.photoFile = imageFile
                setPhoto(imageFile)
            }*/
      //  })
    }
   private fun setPhoto( file: File?) {
        val picasso = Picasso.with(context)
       Picasso.with(activity).load(file).into(myAvatar)



       /* card?.photo?.pictureName?.let {
            picasso.load(ImageUtils.getImagePath(it))
                .resize(card.photo!!.width, card.photo!!.height)
                .into(photo)
        } ?: run {
            file?.let {
                picasso.load(it)
                    .into(photo)
            }*/
        }
    }

    fun getMyEdit()
    {
        //todo лучше потом поставить текст вотчеры

    }
