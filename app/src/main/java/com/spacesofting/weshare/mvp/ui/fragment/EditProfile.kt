package com.spacesofting.weshare.mvp.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.FragmentTransaction
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.visible
import com.pawegio.kandroid.w
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.avatar
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.UpdateProfile
import com.spacesofting.weshare.mvp.presentation.presenter.EditProfilePresenter
import com.spacesofting.weshare.mvp.presentation.view.EditProfileView
import com.spacesofting.weshare.mvp.ui.fragment.InventoryFragment.Companion.AVATAR_CHANGE
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.RealFilePath
import com.spacesofting.weshare.utils.hideKeyboard
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import moxy.presenter.InjectPresenter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class EditProfile : FragmentWrapper(),
    EditProfileView {

  /*  override fun showNewInfo(newinfo: User) {phone
        phone.setText(newinfo.phone.toString(), TextView.BufferType.EDITABLE)
        nickName.setText(newinfo.firstName .toString(), TextView.BufferType.EDITABLE)
        name.setText(newinfo.lastName.toString(), TextView.BufferType.EDITABLE)
        date.setText(getStringForDate(newinfo.birthday.toString()), TextView.BufferType.EDITABLE)
    }*/

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_edit_profile
    }

    val CAMERA_REQUEST_CODE = 1
    val GALLERY_REQUEST_CODE = 2

    val REPLACEMENT = Regex("[^a-zA-Zа-яА-Я0-9-._]")
    val updProfile = UpdateProfile()


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

    //todo метод выбора фото
    private fun showPicker() {
        activity?.hideKeyboard()
        picker = ImagePickerFragment()
        picker?.listener = presenter
        picker?.isAvatarForm = false
        picker?.pathImage = pathImg
        picker?.file = presenter.imageFile
        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    picker?.galleryPermissionGranted = granted
                    fragmentManager?.beginTransaction()
                        ?.addToBackStack(null)
                        ?.add(R.id.container, picker!!)
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.commit()
                    ApplicationWrapper.context = this.context!!
                }
        }
    }

    @InjectPresenter
    lateinit var presenter: EditProfilePresenter

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  showProgress(false)
        showToolbar(TOOLBAR_HIDE)
        presenter
        //handle nick changes and validate it
        RxTextView.afterTextChangeEvents(nickName)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tvChangeEvent ->
                //remove unacceptable symbols
                tvChangeEvent
                val newValue = nickName.text.toString()?.replace(REPLACEMENT, "")
                if (newValue != null && newValue != nickName.text.toString()) {
                    nickName.text.replace(0, nickName.text.length, newValue)
                }
                presenter.fieldChanged(newValue, EditProfilePresenter.Field.NICK)
            }

        nickName.setOnFocusChangeListener { view, isFocused ->
            runDelayed(250) {
                //scroll to the end when edit selected
                if (isFocused) {
                    //   scroll.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
        nickName.setOnClickListener { view ->
            runDelayed(250) {
                //scroll to the end when edit clicked
                //   scroll.fullScroll(View.FOCUS_DOWN)
            }
        }

        editAvatar.setOnClickListener {
            // showPhotoPicker()
            showPicker()
        }
        delPhoto.setOnClickListener { presenter.deletePhoto() }

        saveProfile.setOnClickListener {
            // profile: User
            //todo верификацияs
            //todo если ок то отсылка
            val profila = Settings.get()
            val t1 = nickName.text.toString() == profila?.firstName
            val t2 = phone.text.toString() == profila?.phone
            val t3 = name.text.toString() == profila?.lastName
            Log.e("taras", t1.toString())
            Log.e("taras", t2.toString())
            Log.e("taras", t3.toString())
            if (t1 && t2 && t3) {
                router?.exitWithResult(AVATAR_CHANGE, profila)
            } else {
                updProfile.firstName = nickName.text.toString()
                updProfile.phone = phone.text.toString()
                updProfile.lastName = name.text.toString()
                //  updProfile.birthday =  "2019-09-25T20:09:17.259Z" //date.text.toString()
                presenter.changeMyProfile(updProfile)
            }
        }
    }
  /*  override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/

    override fun showProgress(isInProgress: Boolean) {
        if (isInProgress) {
            contentProfile.visibility = View.INVISIBLE
        } else {
            contentProfile.visibility = View.VISIBLE
        }
       // saveProfile.isEnabled = !isInProgress
        progress.visible = isInProgress
    }

    override fun showToast(stringId: Int) {
        //Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
    }
    override fun updateAvatar(img: String?) {
     //   avatar = img.url.toString()

        if (img != null)
        {
            Picasso.with(context)
                .load(img)
                .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(userAvatar,
                    object : Callback {
                        override fun onSuccess() {
                            // loadImageProgress.visibility = View.GONE
                            delPhoto.visible = true
                        }
                        override fun onError() {
                            //  loadImageProgress.visibility = View.GONE
                        }
                    })
        }

    }


    override fun showProfile(profile: User) {
        //load image todo узнать как загрузить аватарку?
        //load Nickname
        phone.setText(profile.phone.toString(), TextView.BufferType.EDITABLE)
        nickName.setText(profile.firstName .toString(), TextView.BufferType.EDITABLE)
        name.setText(profile.lastName.toString(), TextView.BufferType.EDITABLE)
        updateAvatar(profile.avatar)
      //  date.setText(getStringForDate(profile.birthday.toString()), TextView.BufferType.EDITABLE)
       // val avatar = profile.avatar
      //  ApplicationWrapper.avatar = avatar.toString()

      //  ApplicationWrapper.file = profile.avatar as File
        /*nickName.setText(profile.firstName)
        phone.setText(profile.phone)
        name.setText(profile.lastName)
        date.setText(profile.birthday)
        profileNick = profile.firstName*/
        //hide progress
        showProgress(false)


      //  saveProfile.isEnabled = true
       /* if (profile.avatar != null)
        {
            Picasso.with(context)
                .load(profile.avatar!!.url)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(avatarO,
                    object : Callback {
                        override fun onSuccess() {
                            progress.visibility = View.GONE
                            delPhoto.visible = true
                        }
                        override fun onError() {
                            progress.visibility = View.GONE

                        }
                    })
        }
        else {
          //  delPhoto.visible = false
        }*/
    }
/*    override fun showAvatar(profile: Profile) {
        if (profile.img != null && (profile.img as Image).name != null) {
            val path = ImageUtils.resolveImagePath(profile.img!!.name!!)
            pathImg = path
            Picasso.with(activity).load(path).centerCrop().resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit).into(avatar)
            Picasso.with(activity).load(R.drawable.ic_pen_circle_orange).into(actionIcon)
        } else {
            Picasso.with(activity).load(R.drawable.ic_approve).into(actionIcon)
        }
    }*/

    override fun setTitle(resString: Int) {
        editProfileTitle.text = getString(resString)
    }

    override fun saved(isSuccess: Boolean, isNew: Boolean) {
        if (isSuccess) {
            if (isNew) {
                ApplicationWrapper.instance?.setNewUserFlag(true)
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

    override fun deletePhotos()
    {
        avatar = R.drawable.ic_avatar_placeholder.toString()
        progress.visible = true
        Picasso.with(context)
            .load(avatar)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .centerCrop()
            .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
            .into(userAvatar,
                object : Callback {
                    override fun onSuccess() {
                        progress.visibility = View.GONE
                        delPhoto.visible = false
                    }
                    override fun onError() {
                        progress.visibility = View.GONE
                        delPhoto.visible = false
                    }
                })

    }

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
                  //  mEditProfilePresenter.save()
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
        if (!presenter.hasProfile()) {
          //  router.navigateTo(ScreenPool.FEED_FRAGMENT)
            router.backTo(ScreenPool.INVENTORY_FRAGMENT)
        }

    //    finish()
    }

    fun showPhotoPicker() {
        activity?.hideKeyboard()
        picker = ImagePickerFragment()
        picker?.listener = presenter
        picker?.isAvatarForm = false
        picker?.pathImage = pathImg
        picker?.file = presenter.imageFile
        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    picker?.galleryPermissionGranted = granted
                    fragmentManager?.beginTransaction()
                        ?.addToBackStack(null)
                        ?.add(R.id.container, picker!!)
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.commit()
                    ApplicationWrapper.context = this.context!!
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
        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.CAMERA)
                .subscribe {
                    try {
                        // brokenUrlMessage.visible = false
                        val intent = ImageUtils.takePhotoIntent(file)
                        startActivityForResult(intent, CAMERA_REQUEST_CODE)
                    } catch (e: SecurityException) {
                        e
                        //  dialogGPS(this.context) // lets the user know there is a problem with the gps
                    }
                    //   val  location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
        }
    }
    override fun showImage(file: File) {
        //show image
        Picasso.with(context).load(file).centerCrop().resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit).into(userAvatar)
       // Picasso.with(activity).load(R.drawable.ic_pen_circle_orange).into(actionIcon)
        newAvatar = true
    }

    override fun setPreviewPhoto(file: File) {
        picker.setImage(file)
        if (file != null)
        {
            Picasso.with(context)
                .load(file)
                .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(userAvatar,
                    object : Callback {
                        override fun onSuccess() {
                            // loadImageProgress.visibility = View.GONE
                            delPhoto.visible = true
                        }
                        override fun onError() {
                            //  loadImageProgress.visibility = View.GONE
                        }
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val code = resultCode

        if (code == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    presenter.onCameraResult()
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
                presenter.onGalleryResult(file)
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
      //  saveProfile.isEnabled = isEnabled
    }
    fun getStringForDate(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+ss:ss")
        val f = SimpleDateFormat("yyyy-MM-dd")
        var d = Date()
        try {
            d = formatter.parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return f.format(d)

    }
}
