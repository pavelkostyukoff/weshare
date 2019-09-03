package com.spacesofting.weshare.ui.fragment

import android.app.FragmentTransaction
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.view.AddGoodsView
import com.spacesofting.weshare.presentation.presenter.AddGoodsPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.utils.hideKeyboard
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_add_goods.*

class AddGoodsFragment : FragmentWrapper(), AddGoodsView {
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
        picker?.listener = presenter
        picker?.isAvatarForm = false
        picker?.pathImage = pathImg
        picker?.file = presenter.imageFile

        RxPermissions(activity)
            .request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe { granted ->
                picker?.galleryPermissionGranted = granted
                fragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.container, picker)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
    }
}
