package com.spacesofting.weshare.mvp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.work.*
import com.spacesofting.weshare.common.ActivityWrapper
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.ui.fragment.SplashFragment
import com.spacesofting.weshare.utils.RefreshTokenWorker
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import sun.jvm.hotspot.utilities.IntArray
import java.util.concurrent.TimeUnit


class MainActivity  : ActivityWrapper() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        lockDrawerMenu(false)
        router.navigateTo(ScreenPool.SPLASH_FRAGMENT)
        ApplicationWrapper.context = this.applicationContext
        ApplicationWrapper.INSTANCE.getComponent()?.injectsMainActivity(this)

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        var request = PeriodicWorkRequestBuilder<RefreshTokenWorker>(15, TimeUnit.MINUTES)

            .setConstraints(constraints)
            .build()
/*
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork("jobTag", ExistingPeriodicWorkPolicy.KEEP, request)*/
        WorkManager.getInstance(this).enqueue(request)

       /* WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
            .observe(this, Observer {

                val status: String = it.state.name
                Toast.makeText(this,status, Toast.LENGTH_SHORT).show()
            })*/
    }

    override fun onResume() {
        super.onResume()
        ApplicationWrapper.INSTANCE.getNavigationHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        ApplicationWrapper.INSTANCE.getNavigationHolder().removeNavigator()
    }

    var fragment: androidx.fragment.app.Fragment? = null
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
//todo должно помоч сделать переходы ровные без логических ошибок
//    override fun onBackPressed() {
//       router.exit()
//    }
}