package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.view.AddGoodsView
import com.spacesofting.weshare.presentation.presenter.AddGoodsPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter

class AddGoodsFragment : MvpFragment(), AddGoodsView {
    companion object {
        const val TAG = "AddGoodsFragment"

        fun getInstance(): AddGoodsFragment {
            val fragment: AddGoodsFragment = AddGoodsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mAddGoodsPresenter: AddGoodsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_goods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
