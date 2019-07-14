package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.pawegio.kandroid.runDelayed
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.Settings

class SplashFragment : FragmentWrapper() {
    companion object {
        fun getInstance() = SplashFragment()
    }

    private val DELAY: Long = 1500

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_splash
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)

        runDelayed(DELAY){
            if (Settings.IsAuthorized) {
                ApplicationWrapper.INSTANCE.getRouter().newRootScreen(ScreenPool.REGISTRATION_FRAGMENT)
            } else {
                ApplicationWrapper.INSTANCE.getRouter().newRootScreen(ScreenPool.REGISTRATION_FRAGMENT)
            }
        }
    }
}