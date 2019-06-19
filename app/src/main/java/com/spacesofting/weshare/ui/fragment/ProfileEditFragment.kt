package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.view.ProfileEditView
import com.spacesofting.weshare.presentation.presenter.ProfileEditPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.UpdateProfile
import kotlinx.android.synthetic.main.fragment_profile_edit.*


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
    lateinit var mPresenter: ProfileEditPresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phone.text =
            firstName.text =



        user

        goNewProfile.setOnClickListener {
            val updProfile = UpdateProfile()
            updProfile.phone = phone.text.toString()
            updProfile.firstName = firstName.text.toString()
            updProfile.lastName = lastName.text.toString()
            updProfile.birthday = birthday.text.toString()
            mPresenter.chengeMyProfile(updProfile)//todo класс что кладем)

        }
    }
}
