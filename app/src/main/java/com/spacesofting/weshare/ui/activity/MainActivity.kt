package com.spacesofting.weshare.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import com.spacesofting.weshare.common.ActivityWrapper
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.R
import com.spacesofting.weshare.ui.fragment.SplashFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class MainActivity  : ActivityWrapper() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        lockDrawerMenu(false)
        router.newRootScreen(ScreenPool.SPLASH_FRAGMENT)
    }

    override fun onResume() {
        super.onResume()
        ApplicationWrapper.INSTANCE.getNavigationHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        ApplicationWrapper.INSTANCE.getNavigationHolder().removeNavigator()
    }


    var fragment: Fragment? = null
    override fun onNewIntent(intent: Intent) {

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

        override fun createFragment(screenKey: String, data: Any?): Fragment? {
            return ScreenPool.getFragment(screenKey, data)
        }

        override fun showSystemMessage(message: String?) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }

        override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction?) {
            fragmentTransaction?.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.slide_to_left)
        }
    }
}