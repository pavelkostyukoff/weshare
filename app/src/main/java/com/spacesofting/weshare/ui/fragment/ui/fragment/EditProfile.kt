package com.spacesofting.weshare.ui.fragment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.ui.fragment.R
import com.spacesofting.weshare.ui.fragment.presentation.view.EditProfileView
import com.spacesofting.weshare.ui.fragment.presentation.presenter.EditProfilePresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter

class EditProfile : MvpFragment(), EditProfileView {
    companion object {
        const val TAG = "EditProfile"

        fun newInstance(): EditProfile {
            val fragment: EditProfile = EditProfile()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mEditProfilePresenter: EditProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
