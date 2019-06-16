package com.spacesofting.weshare.ui.fragment.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.ui.presentation.view.IShootItView
import com.spacesofting.weshare.ui.presentation.presenter.IShootItPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper

class IShootItFragment : FragmentWrapper(), IShootItView {

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_irent
    }

    companion object {
        const val TAG = "IShootItFragment"

        fun newInstance(): IShootItFragment {
            val fragment: IShootItFragment =
                IShootItFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mIShootItPresenter: IShootItPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)

    }
}
