package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.presentation.view.ShowGoodView
import com.spacesofting.weshare.presentation.presenter.ShowGoodPresenter

import moxy.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.domain.common.FragmentWrapper

class ShowGoodFragment : FragmentWrapper(), ShowGoodView {
    override fun getFragmentLayout(): Int {
       return R.layout.fragment_show_good
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
    lateinit var mShowGoodPresenter: ShowGoodPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
