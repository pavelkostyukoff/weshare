package com.spacesofting.weshare.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.view.ProfileEditView
import com.spacesofting.weshare.presentation.presenter.ProfileEditPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.UpdateProfile
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File


class ProfileEditFragment : FragmentWrapper(), ProfileEditView {

    val user = ApplicationWrapper.user
    override fun showProgress(b: Boolean) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFragmentLayout(): Int {
return R.layout.fragment_profile_edit
    }

    companion object {
        const val TAG = "ProfileEditFragment"

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
        goNewProfile.setOnClickListener {

            showToolbar(TOOLBAR_INDICATOR_BACK_ARROW)

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

        goNewProfile2.setOnClickListener {

            presenter.savePhoto()
        }

      /*  editPhoto.setOnClickListener {
            mPresenter

            //todo а в презентере viewState который перешлет фото сюда и поменяет его через пикассо
        }*/
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
        EasyImage.openGallery(this, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                presenter.photoFile = imageFile
                setPhoto(imageFile)
            }
        })
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

   /* fun getMyEdit()
    {
        //todo лучше потом поставить текст вотчеры
        phone.setText(user.phone.toString(), TextView.BufferType.EDITABLE)
        firstName.setText(user.firstName.toString(), TextView.BufferType.EDITABLE)
        lastName.setText(user.lastName.toString(), TextView.BufferType.EDITABLE)
        birthday.setText(user.birthday.toString(), TextView.BufferType.EDITABLE)
    }*/
