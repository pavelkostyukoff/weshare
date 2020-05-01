package com.spacesofting.weshare.mvp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import moxy.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.presentation.presenter.BasePresenter
import com.spacesofting.weshare.mvp.presentation.view.BaseView

class BaseFragment : FragmentWrapper(),
    BaseView {
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_base
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHamburgerForRole()
    }

    companion object {
        const val TAG = "BaseFragment"
        fun newInstance(parement: String?): BaseFragment {
            val fragment: BaseFragment =
                BaseFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mBasePresenter: BasePresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_EMBEDDED, R.layout.view_fragment_toolbar)
        setHomeAsUpIndicator(TOOLBAR_INDICATOR_HAMBURGER, R.color.black)
        setTitleColor(R.color.black)
        setTitle(getString(R.string.app_name))
        setToolbarBackgroundDrawable(R.color.link_water)
        // showProgressBar(false)
        setHomeAsUpIndicator(TOOLBAR_INDICATOR_HAMBURGER)


      //  mBasePresenter.getProfile()
    }
}
