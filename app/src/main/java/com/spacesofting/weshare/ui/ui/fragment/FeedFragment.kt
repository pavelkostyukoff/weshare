package com.spacesofting.weshare.ui.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.ui.presentation.view.FeedView
import com.spacesofting.weshare.ui.presentation.presenter.FeedPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper

class FeedFragment : FragmentWrapper(), FeedView {
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_feed
    }

    companion object {
        const val TAG = "FeedFragment"

        fun newInstance(): FeedFragment {
            val fragment: FeedFragment = FeedFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mFeedPresenter: FeedPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
