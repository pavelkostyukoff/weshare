package com.spacesofting.weshare.ui.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.ui.presentation.view.MapView
import com.spacesofting.weshare.ui.presentation.presenter.MapPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper

class MyMapFragment : FragmentWrapper(), MapView {
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_map
    }

    companion object {
        const val TAG = "MyMapFragment"

        fun newInstance(): MyMapFragment {
            val fragment: MyMapFragment = MyMapFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mMapPresenter: MapPresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
