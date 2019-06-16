package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.view.ProfileEditView
import com.spacesofting.weshare.presentation.presenter.ProfileEditPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.common.FragmentWrapper

class ProfileEditFragment : FragmentWrapper(), ProfileEditView {
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
    lateinit var mProfileEditPresenter: ProfileEditPresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
