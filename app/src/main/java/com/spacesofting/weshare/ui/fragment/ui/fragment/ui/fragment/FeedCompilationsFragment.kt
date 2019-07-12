package com.spacesofting.weshare.ui.fragment.ui.fragment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.ui.fragment.ui.fragment.R
import com.spacesofting.weshare.ui.fragment.ui.fragment.presentation.view.FeedCompilationsView
import com.spacesofting.weshare.ui.fragment.ui.fragment.presentation.presenter.FeedCompilationsPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter

class FeedCompilationsFragment : MvpFragment(), FeedCompilationsView {
    companion object {
        const val TAG = "FeedCompilationsFragment"

        fun newInstance(): FeedCompilationsFragment {
            val fragment: FeedCompilationsFragment = FeedCompilationsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mFeedCompilationsPresenter: FeedCompilationsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_compilations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
