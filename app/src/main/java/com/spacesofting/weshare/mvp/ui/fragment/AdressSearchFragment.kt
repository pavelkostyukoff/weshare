package com.spacesofting.weshare.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.presentation.presenter.AddressSearchPresenter
import com.spacesofting.weshare.mvp.presentation.view.AddressSearchView
import moxy.presenter.InjectPresenter

class AddressSearchFragment : FragmentWrapper(),
    AddressSearchView {

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_address_search
    }

    companion object {
        const val TAG = "AddressSearch"

        private const val DATA_KEY = "data"
        fun newInstance(searchText: String?): AddressSearchFragment {
            val fragment = AddressSearchFragment()

            searchText?.let {
                val argument = Bundle()
                argument.putSerializable(DATA_KEY, it)
                fragment.arguments = argument
            }
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: AddressSearchPresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
       // val advert = activity?.intent?.getSerializableExtra(DATA_KEY) as Advert?
    }
}
