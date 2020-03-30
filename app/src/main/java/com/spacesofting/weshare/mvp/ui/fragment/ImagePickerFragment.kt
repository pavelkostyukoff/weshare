package com.spacesofting.weshare.mvp.ui.fragment

import android.app.Fragment
import android.app.FragmentTransaction
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.utils.ImageUtils
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.image_picker_fragment.*
import java.io.File
import java.net.MalformedURLException
import java.net.URL

class ImagePickerFragment : FragmentWrapper(), TextWatcher, ViewTreeObserver.OnGlobalLayoutListener {
    var imageFile: File?   = null
    private val CAMERA_REQUEST_CODE = 1


    override fun getFragmentLayout(): Int {
        return R.layout.image_picker_fragment
    }

    fun openCamera(file: File) {
        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.CAMERA)
                .subscribe { granted ->
                    val intent = ImageUtils.takePhotoIntent(file)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                }
        }
        // brokenUrlMessage.visible = false
    }




    interface PickerListener {
        fun onPickerUrlConfirm(url: URL)
        fun onPickerIncorrectUrl(exception: MalformedURLException)
        fun onPickerCameraClick()
        fun onPickerGalleryClick()
        fun onEditPhotoConfirmClick()
    }

    var listener: PickerListener? = null
    var pathImage: String? = null
    var file: File? = null
    var avatar: ImageView? = null
    var uploadBtn: ImageButton? = null
    var linkIcon: ImageView? = null
    var galleryPermissionGranted: Boolean = false
    var isAvatarForm: Boolean = true
    var contentLayout: ConstraintLayout? = null
    //For fix displayed image for some models phones
    val LIMIT_IMAGE_PIXEL = 2500


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAvatar()
        val scrollView = view?.findViewById<ScrollView>(R.id.scrollView)
        val galleryButton = view?.findViewById<LinearLayout>(R.id.pickerGalleryButton)
        avatar = view?.findViewById(R.id.editAvatar)
     //   uploadBtn = view?.findViewById(R.id.uploadBtn)
     //   linkIcon = view?.findViewById(R.id.linkIcon)

       /* if (activity is EditProfile) {
            activity?.scroll?.visibility = View.GONE
        }*/

        if (!isAvatarForm) {
            view?.findViewById<TextView>(R.id.pickerTitle)?.visibility = View.GONE
            editAvatar.visibility = View.GONE
            editImg.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.rootLayout)?.setBackgroundColor(resources.getColor(R.color.profile_background))
        }

   /*     view?.findViewById<MaterialEditText>(R.id.pickerUrlEditText)?.setOnFocusChangeListener { _, isFocused ->
            runDelayed(250, {
                //scroll to the end when edit selected
                if (isFocused) {
                    scrollView?.fullScroll(View.FOCUS_DOWN)
                }
            })
        }*/

      /*  view?.findViewById<MaterialEditText>(R.id.pickerUrlEditText)?.setOnClickListener({
            runDelayed(250, {
                //scroll to the end when edit clicked
                scrollView?.fullScroll(View.FOCUS_DOWN)
            })
        })*/

      //  view?.findViewById<EditText>(R.id.pickerUrlEditText)?.addTextChangedListener(this)
        //camera
        view?.findViewById<LinearLayout>(R.id.pickerCameraButton)?.setOnClickListener { listener?.onPickerCameraClick() }
        //confirm
        view?.findViewById<ImageButton>(R.id.confirm)?.setOnClickListener { confirmPressed() }
        //upload
      //  view?.findViewById<ImageButton>(R.id.uploadBtn)?.setOnClickListener { confirmUrl() }

        if (galleryPermissionGranted) {
            galleryButton?.setOnClickListener { listener?.onPickerGalleryClick() }
        } else {
            galleryButton?.visibility = View.INVISIBLE
        }
        editImg.viewTreeObserver.addOnGlobalLayoutListener(this)
    }


    fun onPickerCameraClick() {
        imageFile = ImageUtils.savePhotoFile()
        imageFile?.let {
            openCamera(it)
        }
    }

    override fun onGlobalLayout() {
        if (file != null && file!!.exists()) {
            setImage(file!!)
        } else if (pathImage != null) {
            listener?.onPickerUrlConfirm(URL(pathImage))
        }
        editImg.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        if (s != null && s.toString() != "") {
            uploadBtn?.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                linkIcon?.setImageDrawable(resources.getDrawable(R.drawable.ic_link_active, null))
            }
        } else {
            uploadBtn?.visibility = View.INVISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                linkIcon?.setImageDrawable(resources.getDrawable(R.drawable.ic_link_inactive, null))
            }
        }
    }
    fun initAvatar()
    {
        if (ApplicationWrapper.avatar?.isNotEmpty()!!) {
            Picasso.with(activity)
                .load(ApplicationWrapper.avatar)
                .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(editAvatar,
                    object : Callback {
                        override fun onSuccess() {
                            loadImageProgress.visibility = View.GONE
                        }

                        override fun onError() {
                            loadImageProgress.visibility = View.GONE
                        }
                    })
        }
    }

    fun setImage(file: File) {
        //todo refactoring
        //fix rarely crash set image, null pointer exception
        if (file == null) {
            Toast.makeText(activity?.applicationContext, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show()
            return
        }

        loadImageProgress.visibility = View.VISIBLE
        if (isAvatarForm) {
            ApplicationWrapper.file = file


            Picasso.with(activity)
                    .load(file)
                    .centerCrop()
                    .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                    .into(avatar,
                            object : Callback {
                                override fun onSuccess() {
                                    loadImageProgress.visibility = View.GONE
                                }
                                override fun onError() {
                                    loadImageProgress.visibility = View.GONE
                                }
                            })

        } else {
            //Fix displayed image for some models phones
            val bitmap: Bitmap? = BitmapFactory.decodeFile(file.path)
            var scaleFactor = 1.0

            bitmap?.let {
                var newW = bitmap.width
                var newH = bitmap.height

                if (bitmap.width > LIMIT_IMAGE_PIXEL || bitmap.height > LIMIT_IMAGE_PIXEL) {
                    val length = if (newW > newH) newW else newH
                    scaleFactor = LIMIT_IMAGE_PIXEL.toDouble() / length
                    newH = (newH.toDouble() * scaleFactor).toInt()
                    newW = (newW.toDouble() * scaleFactor).toInt()
                }

              /*  WishEditActivity.imgWidth = newW
                WishEditActivity.imgHeight = newH*/

                Picasso.with(activity)
                        .load(file)
                        .memoryPolicy(MemoryPolicy.NO_STORE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(newW, newH)
                        .into(editImg,
                                object : Callback {
                                    override fun onSuccess() {
                                        loadImageProgress?.visible = false
                                    }

                                    override fun onError() {
                                        loadImageProgress?.visible = false
                                    }
                                })
            } ?: run {
                loadImageProgress?.visible = false
            }
        }
    }

  /*  fun onBackPressed() {
        activity.hideKeyboard()
        finish()
    }*/

    private fun confirmUrl() {
      //  val urlString = view?.findViewById<EditText>(R.id.pickerUrlEditText)?.text.toString()
      /*  try {
            val url = URL(urlString)
            listener?.onPickerUrlConfirm(url)
        } catch (e: MalformedURLException) {
            Log.e("BottomSheetImagePicker", "Incorrect url: ${e.message}")
            listener?.onPickerIncorrectUrl(e)
        }*/
      //  activity.hideKeyboard()
    }

    private fun confirmPressed() {
      //  activity.hideKeyboard()
        listener?.onEditPhotoConfirmClick()

        finish()
    }

    fun onEditPhotoConfirmClick()
    {

    }
    fun saveAvatar()
    {

    }

    private fun finish() {
        activity!!.fragmentManager
                .beginTransaction()
                .remove(Fragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit()
        fragmentManager?.popBackStack()
        this.onDetach()

       /* if (activity is EditProfile) {
            activity.scroll.visibility = View.VISIBLE
        }*/
    }
}