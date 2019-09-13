package com.spacesofting.weshare.ui.fragment

import android.app.FragmentTransaction
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.view.AddGoodsView
import com.spacesofting.weshare.presentation.presenter.AddGoodsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.hideKeyboard
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_add_goods.*
import java.io.File


class AddGoodsFragment : FragmentWrapper(), AddGoodsView {
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    override fun showWishImage(file: File) {
        Picasso.with(context).load(file).resize(0, 0).into(wishEditImageView)
        mAddGoodsPresenter.imageChanged = true
        mAddGoodsPresenter.imageFile = file
       // mAddGoodsPresenter.fieldChanged(file.path, AddGoodsPresenter.Field.IMAGE)
        addImgLayout.visibility = View.GONE
        wishChangeImgBtn.visibility = View.VISIBLE
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(activity, stringId, Toast.LENGTH_LONG).show()
    }


    override fun showMaterialDialog(stringId: Int) {
        activity?.let {
            MaterialDialog.Builder(it)
                //  .title(title)
                .content(stringId)
                .positiveText(R.string.ok)
                .build()
                .show()
        }
    }


    override fun save() {
        /*if (!mAddGoodsPresenter.isWishEdit()) {
            activity?.finish()
        } else {
            activity?.let {
                MaterialDialog.Builder(it)
                    .title(R.string.dialog_confirm_save)
                    .content(R.string.dialog_confirm_save)
                    .positiveText(R.string.save)
                    .onPositive { _, _ ->
                       // mAddGoodsPresenter.save()
                    }
                  //  .negativeText(R.string.reset)
                    .onNegative { _, _ ->
                        activity!!.finish()
                    }
                    .show()
            }
        }*/
    }

    override fun cancel() {
        activity?.let {
            MaterialDialog.Builder(it)
                .title(R.string.wish_edit_picker_title)
                .content(R.string.wish_edit_add_img_obligatorily)
                .positiveText(R.string.edit_guest_card_inn_count)
                .onPositive { _, _ ->
                    activity!!.finish()
                }
                .neutralText(R.string.edit)
                .show()
        }
    }

    override fun showProgress(isShowed: Boolean) {
        progressDialog?.let {
            if(isShowed) {
                it.setMessage(getString(R.string.wish_edit_progress))
                it.show()
            } else if (it.isShowing){
                it.dismiss()
            }
        }
    }

    override fun setConfirmButtonState(isEnabled: Boolean) {
        save.isEnabled = isEnabled
    }

    override fun saved(isSuccess: Boolean) {
       /* if (isSuccess) {
            val wish = if (mAddGoodsPresenter.wish != null) mAddGoodsPresenter.wish else mAddGoodsPresenter.newWish
            mAddGoodsPresenter.wish?.isNew().let {
                //   logEvent("wish_created")
            }

            wishSaveSuccess(wish)
            returnWish()
            goToAddCardFragment()
        }
        if (!Settings.isAuthenticated()) {
            goToRegister()
        }*/
    }
    override fun openCamera(file: File) {
        brokenUrlMessage.visible = false
        val intent = ImageUtils.takePhotoIntent(file)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }
    override fun openGallery() {
        val getContentIntent = Intent(Intent.ACTION_GET_CONTENT)
        getContentIntent.type = "image/*"
        getContentIntent.addCategory(Intent.CATEGORY_OPENABLE)
        val intent = Intent.createChooser(getContentIntent, "Select image")
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun emptyPrice(isEmpty: Boolean) {
        if (isEmpty) {
            emptyMessage.visibility = View.VISIBLE
            connectBankCard.visibility = View.GONE
            ymLimitRedLine.visibility = View.GONE
        } else {
            emptyMessage.visibility = View.GONE
        }
    }

    override fun setPreviewImg(file: File) {
        picker?.setImage(file)
    }

    override fun goToRegistration() {
      //  goToRegister()
    }

    override fun showBrokenUrlMessage(isShowed: Boolean) {
        brokenUrlMessage.visible = isShowed
    }

    override fun showCheckUrlProgress(isShowed: Boolean) {
        checkUrlProgress.visible = isShowed

        if (wishUrlEditText.text.isNotEmpty()) {
            clearUrl.visible = !isShowed
        }
    }

    override fun showClearUrlBtn(isShowed: Boolean) {
        if (!checkUrlProgress.visible) {
            clearUrl.visible = isShowed
            getUrl.visible = !isShowed
        }
    }

    override fun showAutoCompleteProgress(isShowed: Boolean) {
        autoCompleteProgress.visible = isShowed
    }

    override fun getFragmentLayout(): Int {
return R.layout.fragment_add_goods
    }

    companion object {
        const val TAG = "AddGoodsFragment"

        fun getInstance(): AddGoodsFragment {
            val fragment: AddGoodsFragment = AddGoodsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mAddGoodsPresenter: AddGoodsPresenter
    var picker: ImagePickerFragment? = null
    var pathImg: String? = null
    var progressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishEditImageBtn.setOnClickListener { showPicker() }

    }

    private fun showPicker() {
        activity?.hideKeyboard()

        picker = ImagePickerFragment()
        picker?.listener = mAddGoodsPresenter
        picker?.isAvatarForm = false
        picker?.pathImage = pathImg
        picker?.file = mAddGoodsPresenter.imageFile

        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    picker?.galleryPermissionGranted = granted
                    fragmentManager?.beginTransaction()
                        ?.addToBackStack(null)
                        ?.add(com.spacesofting.weshare.R.id.container, picker)
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.commit()
                }
        }
    }


/*    fun onBackPressed() {
        val fragment = fragmentManager!!.findFragmentByTag("name")
        if (fragment != null && fragment.isVisible) {
            //  do something
        } else {
            super.onBackPressed()
        }
    }*/
}
