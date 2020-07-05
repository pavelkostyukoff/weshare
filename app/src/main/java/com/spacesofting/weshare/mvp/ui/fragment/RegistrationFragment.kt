package com.spacesofting.weshare.mvp.ui.fragment

import android.app.Activity
import android.app.FragmentTransaction
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import moxy.presenter.InjectPresenter
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.toast
import com.pawegio.kandroid.visible
import com.pawegio.kandroid.w
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.avatar
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.MailComfirm
import com.spacesofting.weshare.mvp.model.Photo
import com.spacesofting.weshare.mvp.presentation.presenter.RegistrationPresenter
import com.spacesofting.weshare.mvp.presentation.view.EditProfileView
import com.spacesofting.weshare.mvp.presentation.view.RegistrationView
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.RealFilePath
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.avatarO
import kotlinx.android.synthetic.main.fragment_registration.background
import kotlinx.android.synthetic.main.fragment_registration.contentProfile
import kotlinx.android.synthetic.main.fragment_registration.correctNickLayout
import kotlinx.android.synthetic.main.fragment_registration.correctNickText
import kotlinx.android.synthetic.main.fragment_registration.datePicker
import kotlinx.android.synthetic.main.fragment_registration.delPhoto
import kotlinx.android.synthetic.main.fragment_registration.incorrectNickLayout
import kotlinx.android.synthetic.main.fragment_registration.incorrectNickText
import kotlinx.android.synthetic.main.fragment_registration.progress
import kotlinx.android.synthetic.main.fragment_registration.saveProfile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class RegistrationFragment : FragmentWrapper(),
    RegistrationView {
    private var comfirmDlg: AlertDialog? = null

    override fun showEmailComfirmDialog(login: String?) {
        //todo show dialog get text and send presenter comfirm
        comfirmDlg(true,login)
        //TODO берем почту и автоматом отправляем юзеру
    }
    override fun toastError(s: String) {
        toast(s)
    }
    fun comfirmDlg(isVisible: Boolean, login: String?) {
        if (isVisible) {
            val view = LayoutInflater.from(activity).inflate(R.layout.confirm_dialog, null)

            view.findViewById<Button>(R.id.ok).setOnClickListener {
                val mailComfirm = MailComfirm()
                mailComfirm.code = view.findViewById<EditText>(R.id.code)?.text.toString()
                mailComfirm.login = login
                regPresenter.comfirmMail(mailComfirm)
                comfirmDlg?.dismiss()
            }

            comfirmDlg = activity?.let {
                AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .show()
            }
        } else {
            comfirmDlg?.dismiss()
        }
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_registration
    }

    val CAMERA_REQUEST_CODE = 1
    val GALLERY_REQUEST_CODE = 2
    val SCANNER_REQUEST_CODE = 101
    var firstStart = true


    val REPLACEMENT = Regex("[^a-zA-Zа-яА-Я0-9-._]")
    val profile = Profile()


    var picker: ImagePickerFragment by Delegates.notNull()
    var pathImg: String? = null
    var profileNick: String? = null
    var newAvatar: Boolean = false
    companion object {
        const val TAG = "EditProfile"

        fun newInstance(): RegistrationFragment {
            val fragment: RegistrationFragment =
                RegistrationFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var regPresenter: RegistrationPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // showProgress()
        showToolbar(TOOLBAR_HIDE)
        regPresenter
        //handle nick changes and validate it
       /* RxTextView.afterTextChangeEvents(loginText)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tvChangeEvent ->

                //remove unacceptable symbols{
                val newValue = loginText.text.toString()?.replace(REPLACEMENT, "")
                if (newValue != null && newValue != loginText.text.toString()) {
                    loginText.text.replace(0, loginText.text.length, newValue)
                }

                regPresenter.fieldChanged(newValue, RegistrationPresenter.Field.NICK)
            }*/

      /*  loginText.setOnFocusChangeListener { view, isFocused ->
            runDelayed(250) {
                //scroll to the end when edit selected
                if (isFocused) {
                    scroll.fullScroll(View.FOCUS_DOWN)
                }
            }
        }*/
        loginText.setOnClickListener { view ->
            runDelayed(250) {
                //scroll to the end when edit clicked
               // scroll.fullScroll(View.FOCUS_DOWN)
            }
        }

        avatarO.setOnClickListener { showPhotoPicker() }
        delPhoto.setOnClickListener { regPresenter.deletePhoto() }

        saveProfile.setOnClickListener {
            // profile: User
            //todo верификация
            //profile если ок то отсылка
            //todo валидация паттерн для почты
            profile.login = "pavel.rilisoft+" + loginText.text.toString() + "@gmail.com"

            //  profile.login = "Soprano61+" + loginText.text.toString() + "@yandex.ru"
            //todo валидация паттерн для почты
            profile.password = "password"//password.text.toString()
            //todo валидация bvtyb
            profile.firstName = "POUL"//firstName.text.toString()
            profile.lastName = "KOSTEN"// lasftName.text.toString()
            //todo валидация телефона и изменения 8 на +7
            profile.phone = "+79515250250"//phone.text.toString()
            //todo привидение к виду -> 1988-01-01
            profile.birthday = "1988-01-01"//getUserDate()

            //  updProfile.birthday =  "2019-09-25T20:09:17.259Z" //date.text.toString()
            //todo отправляем на регу и открываем диалог для ввода кода 4 символа
            regPresenter.setRegistration(profile)
        }
    }
    fun getUserDate(): String {
        var strDate = ""
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val d = Date(year, month, day)
         strDate = dateFormatter.format(d)
        return strDate
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

    override fun showProfile(profile: User) {
    }

    override fun showToast(stringId: Int) {
        //Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
    }
    override fun updateAvatar(img: Photo) {
        avatar = img.url.toString()
        if (img.url != null)
        {
            Picasso.with(context)
                .load(img.url.toString())
                .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(avatarO,
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
    //    editProfileTitle.text = getString(resString)
    }

    override fun saved(isSuccess: Boolean, isNew: Boolean) {
        if (isSuccess) {
            if (isNew) {
                ApplicationWrapper.instance.setNewUserFlag(true)
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
            .into(avatarO,
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

    override fun onStart() {
        super.onStart()
        background.startScrollingAnimation(firstStart)
        firstStart = false
      //  runDelayed(2000) { background.startDimmingAnimation() }
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

    override fun errorDlg(message: String) {
        activity?.let {
            MaterialDialog.Builder(it)
                .title(R.string.error_general)
                .content(message)
                .positiveText(R.string.ok)
                .onPositive { dialog, which ->
                    close()
                }
                .neutralText(R.string.edit)
                .show()
        }
    }

    override fun close() {
        if (!regPresenter.hasProfile()) {
          //  router.navigateTo(ScreenPool.FEED_FRAGMENT)
            router.backTo(ScreenPool.INVENTORY_FRAGMENT)
        }

    //    finish()
    }

    fun showPhotoPicker() {
        picker = ImagePickerFragment()
        picker.listener = regPresenter
        picker.pathImage = pathImg
        picker.file = regPresenter.imageFile

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
        Picasso.with(context).load(file).centerCrop().resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit).into(avatarO)
       // Picasso.with(activity).load(R.drawable.ic_pen_circle_orange).into(actionIcon)
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
                    regPresenter.onCameraResult()
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
                regPresenter.onGalleryResult(file)
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
