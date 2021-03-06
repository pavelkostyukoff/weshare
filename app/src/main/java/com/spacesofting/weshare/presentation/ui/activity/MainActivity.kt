package com.spacesofting.weshare.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.common.ActivityWrapper
import com.spacesofting.weshare.presentation.common.ApplicationWrapper
import com.spacesofting.weshare.presentation.common.ScreenPool
import com.spacesofting.weshare.presentation.ui.fragment.SplashFragment
import com.spacesofting.weshare.presentation.ui.widget.ActionBottomDialogFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command


class MainActivity  : ActivityWrapper(), ActionBottomDialogFragment.ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        lockDrawerMenu(false)
        router.navigateTo(ScreenPool.SPLASH_FRAGMENT)
        ApplicationWrapper.context = this.applicationContext
    }

    override fun onResume() {
        super.onResume()
        ApplicationWrapper.instance.getNavigationHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        ApplicationWrapper.instance.getNavigationHolder().removeNavigator()
    }

    var fragment: androidx.fragment.app.Fragment? = null

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Check if the fragment is an instance of the right fragment
        if (fragment is SplashFragment) {
            val my = fragment as SplashFragment?
            setIntent(intent)
        }
    }

    private fun showNFCSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        startActivity(intent)
    }

    private val navigator: Navigator = object: SupportAppNavigator(this, R.id.mainContainer){
        override fun exit() {
            // showLogoutDialogProcess()
            finish()
        }

        override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? {
            return ScreenPool.getActivity(context, screenKey, data)
        }

        override fun createFragment(screenKey: String, data: Any?): androidx.fragment.app.Fragment? {
            return ScreenPool.getFragment(screenKey, data)
        }

        override fun showSystemMessage(message: String?) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }

        override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: androidx.fragment.app.Fragment?, nextFragment: androidx.fragment.app.Fragment?, fragmentTransaction: androidx.fragment.app.FragmentTransaction?) {
            fragmentTransaction?.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.slide_to_left)
        }
    }

    override fun onItemClick(item: String?) {
        TODO("Not yet implemented")
    }

//todo ???????????? ?????????? ?????????????? ???????????????? ???????????? ?????? ???????????????????? ????????????
//    override fun onBackPressed() {
//       router.exit()
//    }
}