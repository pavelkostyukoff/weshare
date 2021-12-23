package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.spacesofting.weshare.R
import com.spacesofting.weshare.data.api.RespouncePublish
import com.spacesofting.weshare.domain.common.ApplicationWrapper
import com.spacesofting.weshare.domain.common.FragmentWrapper
import com.spacesofting.weshare.domain.common.ScreenPool
import com.spacesofting.weshare.domain.common.Settings
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.presentation.presenter.InventoryPresenter
import com.spacesofting.weshare.presentation.view.InventoryView
import com.spacesofting.weshare.ui.adapter.InventoryPagerAdapter
import com.spacesofting.weshare.ui.widget.ActionBottomDialogFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_bag_goods.*
import kotlinx.android.synthetic.main.view_bag_my_info.*
import moxy.presenter.InjectPresenter
import java.text.SimpleDateFormat
import java.util.*

class InventoryFragment : FragmentWrapper(),
    InventoryView, ActionBottomDialogFragment.ItemClickListener {
    private var advert: RespouncePublish? = null
    private var tab: Int? = null

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_inventory
    }

    companion object {
        val SCANNER_REQUEST_CODE = 101
        val AVATAR_CHANGE = 101
        private const val POSITION_KEY = "key_pass"
        private const val ADVERT_KEY = "key_card"
        const val TAG = "InventoryFragment"

        fun getBundle(pass: Int?, card: RespouncePublish?) : Bundle{
            val argument = Bundle()
            argument.putSerializable(POSITION_KEY, pass)
            argument.putSerializable(ADVERT_KEY, card)
            return argument
        }

        fun newInstance(bundle: Bundle): InventoryFragment {
            val fragment = InventoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mInventoryPresenter: InventoryPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{ bundle ->
            tab = bundle.get(POSITION_KEY) as? Int
            advert = bundle.get(ADVERT_KEY) as? RespouncePublish
        }

        if(tab == null) {
            tab = 0
        }

        navItemInvited.setOnClickListener {

     /*       val addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance()
            addPhotoBottomDialogFragment.show(
                activity?.getSupportFragmentManager()!!,
                ActionBottomDialogFragment.TAG
            )
*/
        }
        showToolbar(TOOLBAR_HIDE)
        //showToolbar(TOOLBAR_EMBEDDED, R.layout.view_fragment_toolbar)
       // setTitleColor(R.color.black)
      //  setTitle("Bag")
       // setToolbarBackgroundDrawable(R.color.link_water)
       // setHomeAsUpIndicator(TOOLBAR_INDICATOR_BACK_ARROW)
        val fragmentAdapter = InventoryPagerAdapter(childFragmentManager,advert)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)
   /*     tabs_main.getTabAt(0)?.setIcon(R.drawable.ic_message)
        tabs_main.getTabAt(1)?.setIcon(R.drawable.ic_calendar)*/
        tabs_main.getTabAt(tab as Int)?.select()
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
            ApplicationWrapper.instance.setAuthorityWish()
            mInventoryPresenter.createNewAdvert()
        }

        showSettings.setOnClickListener {
            router.navigateTo(ScreenPool.EDIT_PROFILE)
        }

        router.setResultListener(SCANNER_REQUEST_CODE) { result ->
            if (result != null) {
                ApplicationWrapper.user = result as User
             //   showAvatar(ApplicationWrapper.user.avatar)
               // nameUpdate(result as User)
                /*setFoldInfo(result as User)   */         }
        }

        router.setResultListener(AVATAR_CHANGE) { result ->
            if (result != null) {
                Settings.set(result as User)
                ApplicationWrapper.user = result as User
              //  showAvatar(ApplicationWrapper.user.avatar)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        profileUpdate()
    }

    private fun profileUpdate()
    {
        val result  = Settings.get()
      /*  if (result == null)
        {
            mInventoryPresenter.getProfile()
        }
        else {*/
        result?.let {
            pHone.text = it.phone.toString()
            firstName.text = it.firstName.toString()
            lastName.text = it.lastName.toString()
            showAvatar(it.avatar)
        }

            // date.text = result.lastName.toString()
      //  }
      //todo  showAvatar(result.avatar)
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

    private fun showAvatar(avatar: String?)
    {
        if (avatar != null)
        {
            Picasso.with(context)
                .load(avatar)
               // .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(mainAvatar,
                    object : Callback {
                        override fun onSuccess() {
                           //  loadImageProgress.visibility = View.GONE
                        }
                        override fun onError() {
                            //  loadImageProgress.visibility = View.GONE
                        }
                    })
        }
    }

    override fun onItemClick(item: String?) {
        TODO("Not yet implemented")
    }

}
