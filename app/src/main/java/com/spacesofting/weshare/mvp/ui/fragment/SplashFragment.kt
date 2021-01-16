package com.spacesofting.weshare.mvp.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.common.Settings
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : FragmentWrapper() {
    companion object {
        fun getInstance() =
            SplashFragment()
    }

    private val DELAY: Long = 2000
    val animation = AnimationUtils.loadAnimation(ApplicationWrapper.context, R.anim.rotate)

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_splash
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        activity?.scan?.visible = false

        gpbLogo.startAnimation(animation)
    //    scan.visible = false
        Settings.checkAuthorization()

        runDelayed(DELAY){
            if (Settings.isAuthenticated()) {
                router.newRootScreen(ScreenPool.FEED_FRAGMENT)
            } else {
               //todo конкретно тут мы стартуем router.newRootScreen(ScreenPool.AUTORIZE_FRAGMENT)
            router.newRootScreen(ScreenPool.AUTORIZE_FRAGMENT) //AUTORIZE_FRAGMENT FEED_FRAGMENT
            }
        }
    }

    override fun onStop() {
        super.onStop()
      //  scan.visible = true
        activity?.scan?.visible = true

        gpbLogo.clearAnimation()

    }
}