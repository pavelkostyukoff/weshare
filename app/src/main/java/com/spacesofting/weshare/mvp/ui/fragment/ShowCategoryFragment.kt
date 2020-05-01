package com.spacesofting.weshare.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.mvp.presentation.view.ShowCategoryView
import com.spacesofting.weshare.mvp.presentation.presenter.ShowCategoryPresenter

import moxy.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper

class ShowCategoryFragment : FragmentWrapper(), ShowCategoryView {
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_show_category
    }

    companion object {
        val SCANNER_REQUEST_CODE = 101

        const val TAG = "ShowGoodFragment"
        private const val DATA_KEY = "data"
        fun newInstance(tabPosition: String?): ShowGoodFragment {
            val fragment = ShowGoodFragment()

            tabPosition?.let {
                val argument = Bundle()
                argument.putSerializable(DATA_KEY, it)
                fragment.arguments = argument
            }
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mShowCategoryPresenter: ShowCategoryPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
