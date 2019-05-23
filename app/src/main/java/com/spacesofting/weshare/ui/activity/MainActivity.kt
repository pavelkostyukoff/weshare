package com.spacesofting.weshare.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import com.spacesofting.weshare.common.ActivityWrapper
import com.digitalhorizon.eve.common.ApplicationWrapper
import com.digitalhorizon.eve.common.ScreenPool
import com.spacesofting.weshare.R
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class MainActivity  : ActivityWrapper() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //restore theme to the App's one
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        lockDrawerMenu(false)
        router.navigateTo(ScreenPool.SPLASH_FRAGMENT)
    }

    override fun onResume() {
        super.onResume()
        ApplicationWrapper.INSTANCE.getNavigationHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        ApplicationWrapper.INSTANCE.getNavigationHolder().removeNavigator()
    }

    private val navigator: Navigator = object: SupportAppNavigator(this, R.id.mainContainer){
        override fun exit() {
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