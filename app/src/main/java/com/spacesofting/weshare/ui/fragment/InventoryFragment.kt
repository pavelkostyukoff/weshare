package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import com.spacesofting.weshare.mvp.view.InventoryView
import com.spacesofting.weshare.mvp.presentation.InventoryPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.ui.adapter.InventoryPagerAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.view_bag_goods.*
import kotlinx.android.synthetic.main.view_bag_my_info.*
import java.text.SimpleDateFormat
import java.util.*

class InventoryFragment : FragmentWrapper(), InventoryView {
   // val user = ApplicationWrapper.user

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_inventory
    }

    companion object {
        val SCANNER_REQUEST_CODE = 101

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
        showToolbar(TOOLBAR_HIDE)
        //showToolbar(TOOLBAR_EMBEDDED, R.layout.view_fragment_toolbar)
       // setTitleColor(R.color.black)
      //  setTitle("Bag")
       // setToolbarBackgroundDrawable(R.color.link_water)
       // setHomeAsUpIndicator(TOOLBAR_INDICATOR_BACK_ARROW)
        val fragmentAdapter = InventoryPagerAdapter(childFragmentManager)
        viewpager_main.adapter = fragmentAdapter

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



        goodAdd.setOnClickListener {
            router.navigateTo(ScreenPool.ADD_GOODS)
        }

        showSettings.setOnClickListener {
            router.navigateTo(ScreenPool.EDIT_PROFILE)
        }
            router.setResultListener(SCANNER_REQUEST_CODE) { result ->
            if (result != null) {
                ApplicationWrapper.user = result as User
               // nameUpdate(result as User)
                /*setFoldInfo(result as User)   */         }
        }
    }
    fun setFoldInfo(result: User)
    {
        ApplicationWrapper.user = result
       // filterCount.text = (getFiltersState.size + getFiltersPriority.size).toString()

       // firstName.text = result.firstName as String
       // lastName.text = result.lastName as String

    }

    override fun onResume() {
        super.onResume()
        nameUpdate(ApplicationWrapper.user)
    }
    fun nameUpdate(result: User)
    {
        pHone.text = result.phone.toString()
        firstName.text = result.firstName.toString()
        lastName.text = result.lastName.toString()
       // date.text = result.lastName.toString()


        showAvatar(result.avatar)
    }
    fun getStringForDate(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+ss:ss")
        val f = SimpleDateFormat("yyyy-MM-dd")
        var d = Date()
        try {
            d = formatter.parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return f.format(d)

    }

    fun showAvatar(avatar: String?)
    {
        if (avatar != null)
        {
            Picasso.with(activity)
                .load(avatar)
                .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(mainAvatar,
                    object : Callback {
                        override fun onSuccess() {
                            // loadImageProgress.visibility = View.GONE
                        }
                        override fun onError() {
                            //  loadImageProgress.visibility = View.GONE
                        }
                    })
        }
    }
}
