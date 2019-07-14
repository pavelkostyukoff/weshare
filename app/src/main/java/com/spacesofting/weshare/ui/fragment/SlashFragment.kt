package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.spacesofting.weshare.mvp.view.SlashView
import com.spacesofting.weshare.mvp.presentation.SlashPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.pawegio.kandroid.runDelayed
import com.spacesofting.weshare.R
import kotlinx.android.synthetic.main.fragment_slash.*

class SlashFragment : MvpFragment(), SlashView {
    var isAuth = false
    companion object {
        const val TAG = "SlashFragment"

        fun newInstance(): SlashFragment {
            val fragment: SlashFragment =
                SlashFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mSlashPresenter: SlashPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_slash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


    }


    override fun goRegistration() {
       /* startActivity(RegisterActivity.getIntent(this))
        overridePendingTransition(0, 0)
        closeActivity = true*/
    }

    override fun goToNext() {
        if (isAuth) {
            goRegistration()
        } else {
            goToMainScreen()
        }
    }

    override fun onStart() {
        super.onStart()
      //  splashBackground.startScrollingAnimation(firstStart)
       // firstStart = false
        runDelayed(2000, { splashBackground.startDimmingAnimation() })
    }

    override fun onStop() {
        super.onStop()
        splashBackground.stopAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
       // Settings.isFirstRun = false
    }

    override fun onResume() {
        super.onResume()
    /*    if (closeActivity) {
            activity.finish()
        } else {
            mSlashPresenter.init()
        }*/
    }

/*
    override fun onBackPressed() {
        mSplashPresenter.cancelTransition()
        super.onBackPressed()
    }
*/

    override fun goToMainScreen() {
     /*   if (Settings.isFirstRun == true) {
            startActivity(TutorialActivity.getIntent(this, TutorialActivity.TutorialMode.MODE_INITIAL))
            closeActivity = true
            ApplicationWrapper.trackEvent(this, FirebaseAnalytics.Event.TUTORIAL_BEGIN, null, analitics)
            finish()
        } else {
            finish()
            //startActivity(FeedFragment.getIntent(this))
            startActivity(NavigationActivity.getIntent(this, NavigationActivity.PROFILE_SLIDE))
            closeActivity = true
        }*/
    }

}
