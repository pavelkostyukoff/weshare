package com.spacesofting.weshare.ui.fragment.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.ui.presentation.view.InventoryView
import com.spacesofting.weshare.ui.presentation.presenter.InventoryPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ActivityWrapper
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.ui.adapter.LoginPagerAdapter
import kotlinx.android.synthetic.main.view_bag_goods.*
import kotlinx.android.synthetic.main.view_bag_my_info.*

class InventoryFragment : FragmentWrapper(), InventoryView {
    val user = ApplicationWrapper.user

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_inventory
    }

    companion object {
        const val TAG = "InventoryFragment"
        private const val DATA_KEY = "data"
        fun newInstance(tabPosition: Int?): InventoryFragment {
            val fragment = InventoryFragment()

            tabPosition?.let {
                val argument = Bundle()
                argument.putSerializable(DATA_KEY, it)
                fragment.arguments = argument
            }
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mInventoryPresenter: InventoryPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabPosition = arguments?.getSerializable(DATA_KEY)
        //showToolbar(TOOLBAR_EMBEDDED, R.layout.view_fragment_toolbar)
        setTitleColor(R.color.black)
        setTitle("Bag")
        setToolbarBackgroundDrawable(R.color.link_water)
        setHomeAsUpIndicator(TOOLBAR_INDICATOR_BACK_ARROW)
        val fragmentAdapter = LoginPagerAdapter(childFragmentManager)
        viewpager_main.adapter = fragmentAdapter
//data

        tabs_main.setupWithViewPager(viewpager_main)
   /*     tabs_main.getTabAt(0)?.setIcon(R.drawable.ic_message)
        tabs_main.getTabAt(1)?.setIcon(R.drawable.ic_calendar)*/
        tabs_main.getTabAt(tabPosition as Int)?.select()
        tabs_main.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

      /*  presenter.router.setResultListener(QrScanerFragment.SCANNER_REQUEST_CODE) { result ->
            if (result != null && result is String) {
                // presenter.getProfile(Pass(result))
                router.navigateTo(ScreenPool.BASE_FRAGMENT, result)
            }
        }*/
        lastName.text = user.firstName.toString()
        lastName.text = user.lastName.toString()


        showSettings.setOnClickListener {


            router.navigateTo(ScreenPool.PROFILEEDIT)


        }

    }
}
