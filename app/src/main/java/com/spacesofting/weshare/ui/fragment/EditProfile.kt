package com.spacesofting.weshare.ui.fragment

import android.app.Activity
import android.app.FragmentTransaction
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.spacesofting.weshare.ui.fragment.presentation.view.EditProfileView
import com.spacesofting.weshare.ui.fragment.presentation.presenter.EditProfilePresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.visible
import com.pawegio.kandroid.w
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.RealFilePath
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class EditProfile : FragmentWrapper(), EditProfileView {

    override fun showNewInfo(newinfo: User) {
        phoneEdP.setText(newinfo.phone.toString(), TextView.BufferType.EDITABLE)
        firstNameEdP.setText(newinfo.firstName.toString(), TextView.BufferType.EDITABLE)
        lastNameEdP.setText(newinfo.lastName.toString(), TextView.BufferType.EDITABLE)
        birthdayEdP.setText(newinfo.birthday.toString(), TextView.BufferType.EDITABLE)
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_edit_profile
    }

    val CAMERA_REQUEST_CODE = 1
    val GALLERY_REQUEST_CODE = 2
    val REPLACEMENT = Regex("[^a-zA-Zа-яА-Я0-9-._]")


    var picker: ImagePickerFragment by Delegates.notNull()
    var pathImg: String? = null
    var profileNick: String? = null
    var newAvatar: Boolean = false
    companion object {
        const val TAG = "EditProfile"

        fun newInstance(): EditProfile {
            val fragment: EditProfile =
                EditProfile()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mEditProfilePresenter: EditProfilePresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //show progress
        showProgress()

        //handle nick changes and validate it
        RxTextView.afterTextChangeEvents(firstNameEdP)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tvChangeEvent ->
                //remove unacceptable symbols
                val newValue = firstNameEdP.text.toString()?.replace(REPLACEMENT, "")
                if (newValue != null && newValue != firstNameEdP.text.toString()) {
                    firstNameEdP.text.replace(0, firstNameEdP.text.length, newValue)
                }
                mEditProfilePresenter.fieldChanged(newValue, EditProfilePresenter.Field.NICK)
            }

        firstNameEdP.setOnFocusChangeListener { view, isFocused ->
            runDelayed(250) {
                //scroll to the end when edit selected
                if (isFocused) {
                    scroll.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
        firstNameEdP.setOnClickListener { view ->
            runDelayed(250) {
                //scroll to the end when edit clicked
                scroll.fullScroll(View.FOCUS_DOWN)
            }
        }

        avatar.setOnClickListener { showPhotoPicker() }
    }
  /*  override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/

    override fun showProgress(isInProgress: Boolean) {
        if (isInProgress) {
            content.visibility = View.INVISIBLE
        } else {
            content.visibility = View.VISIBLE
        }

        save.isEnabled = !isInProgress
        progress.visible = isInProgress
    }

    override fun showToast(stringId: Int) {
        //Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
    }

    override fun showProfile(profile: Profile) {
        //load image todo узнать как загрузить аватарку?



        //load Nickname
        firstNameEdP.setText(profile.firstName)
        profileNick = profile.firstName
        //hide progress
        showProgress(false)
        save.setOnClickListener { mEditProfilePresenter.save() }
        save.isEnabled = true


    }
   /* override fun showAvatar(profile: Profile) {
        if (profile.img != null && (profile.img as Image).name != null) {
            val path = ImageUtils.resolveImagePath(profile.img!!.name!!)
            pathImg = path
            Picasso.with(activity).load(path).centerCrop().resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit).into(avatar)
            Picasso.with(activity).load(R.drawable.ic_pen_circle_orange).into(actionIcon)
        } else {
            Picasso.with(activity).load(R.drawable.ic_wish_fav_inactive).into(actionIcon)
        }
    }*/



    override fun setTitle(resString: Int) {
        editProfileTitle.text = getString(resString)
    }

    override fun saved(isSuccess: Boolean, isNew: Boolean) {
        if (isSuccess) {
            if (isNew) {
                ApplicationWrapper.INSTANCE.setNewUserFlag(true)
               // logEvent("profile_created")
            }

      /*      if(!Settings.isScenarioFinished && Settings.scenario == ScenarioActivity.SCENARIO_FIRST){
                val intent = ScenarioActivity.getIntent(this, Settings.scenario)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            finish()*/
        } else {
         //   toast(R.string.edit_profile_save_failed)
            showProgress(false)
        }
    }

    /*override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            mEditProfilePresenter.onBackPressed()
        } else {
            picker.onBackPressed()
        }
    }*/

    override fun onResume() {
        super.onResume()
        //animate background
        background.startScrollingAnimation(false)
        background.startDimmingAnimation()
    }

    override fun onPause() {
        super.onPause()
        background.stopAnimation()
    }

    override fun save() {
        activity?.let {
            MaterialDialog.Builder(it)
                .content(R.string.declarer)
                .positiveText(R.string.save)
                .onPositive { dialog, which ->
                    mEditProfilePresenter.save()
                }
                .negativeText(R.string.edit_guest_card_label_first_name)
                .onNegative { dialog, which ->
                    close()
                }
                .show()
        }
    }

    override fun cancel() {
        activity?.let {
            MaterialDialog.Builder(it)
                .title(R.string.search_hint)
                .content(R.string.dialog_chose_action)
                .positiveText(R.string.guest_card_state_send_for_approval)
                .onPositive { dialog, which ->
                    close()
                }
                .neutralText(R.string.edit)
                .show()
        }
    }

    override fun close() {
        if (!mEditProfilePresenter.hasProfile()) {
            router.navigateTo(ScreenPool.FEED_FRAGMENT)
        }

    //    finish()
    }

    fun showPhotoPicker() {
        picker = ImagePickerFragment()
        picker.listener = mEditProfilePresenter
        picker.pathImage = pathImg
        picker.file = mEditProfilePresenter.imageFile

        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    picker.galleryPermissionGranted = granted
                    fragmentManager
                        ?.beginTransaction()
                        ?.addToBackStack(null)
                        ?.add(R.id.container, picker)
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.commit()
                }
        }
    }

    override fun openGallery() {
        val getContentIntent = Intent(Intent.ACTION_GET_CONTENT)
        getContentIntent.type = "*/*"
        getContentIntent.addCategory(Intent.CATEGORY_OPENABLE)
        val intent = Intent.createChooser(getContentIntent, "Select a file")
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun openCamera(file: File) {
        val intent = ImageUtils.takePhotoIntent(file)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun showImage(file: File) {
        //show image
        Picasso.with(activity).load(file).centerCrop().resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit).into(avatar)
        Picasso.with(activity).load(R.drawable.ic_pen_circle_orange).into(actionIcon)
        newAvatar = true
    }

    override fun setPreviewPhoto(file: File) {
        picker.setImage(file)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val code = resultCode

        if (code == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    mEditProfilePresenter.onCameraResult()
                    return
                }
                GALLERY_REQUEST_CODE -> {
                    consumeGalleryResult(data)
                    return
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun consumeGalleryResult(data: Intent?) {
        data?.let {
            val uri = it.data as Uri
            val path = activity?.let { it1 -> RealFilePath.getPath(it1, uri) }

            if (path != null) {
                val file = File(path)
                mEditProfilePresenter.onGalleryResult(file)
            } else {
                w("Could not load path from Gallery")
            }
        }
    }

    override fun onCheckNick(status: Int) {
        incorrectNickLayout.visible = false
        correctNickLayout.visible = false

        when(status){
            EditProfileView.AVAILABLE -> {
                correctNickText.setText(R.string.edit_profile_profile_free_name)
                correctNickLayout.visible = true
            }
            EditProfileView.ALREADY_EXIST -> {
                incorrectNickText.setText(R.string.edit_profile_profile_already_exist_name)
                incorrectNickLayout.visible = true
            }
            EditProfileView.LONG_NICK -> {
                incorrectNickText.setText(R.string.edit_profile_nick_requirements)
                incorrectNickLayout.visible = true
            }
            EditProfileView.SHORT_NICK -> {
                incorrectNickText.setText(R.string.edit_profile_min_len_nick_name)
                incorrectNickLayout.visible = true
            }
            EditProfileView.NONE -> {
                incorrectNickLayout.visible = false
                correctNickLayout.visible = false
            }
        }
    }

    override fun showCheckNickProgress(showing: Boolean) {
        checkNickProgress.visible = showing
    }

    override fun saveButtonEnabled(isEnabled: Boolean) {
        save.isEnabled = isEnabled
    }
}
