package com.spacesofting.weshare.mvp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.work.*
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ActivityWrapper
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.ui.fragment.SplashFragment
import com.spacesofting.weshare.utils.RefreshTokenWorker
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import java.util.concurrent.TimeUnit


class MainActivity  : ActivityWrapper() {
   /* @RequiresApi(Build.VERSION_CODES.O)
    @Inject
    var databaseHelper: DatabaseHelper? = null

    @Inject
    var networkUtils: NetworkUtils? = null*/
   val KEY_TASK_DESC: String? = "key_task_desc"
    private lateinit var workManager: WorkManager
    private lateinit var timeWorkRequest: OneTimeWorkRequest
    private lateinit var periodicWorkRequest: PeriodicWorkRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        lockDrawerMenu(false)
        router.navigateTo(ScreenPool.SPLASH_FRAGMENT)
        ApplicationWrapper.context = this.applicationContext
        ApplicationWrapper.INSTANCE.getComponent()?.injectsMainActivity(this)
        workManager = WorkManager.getInstance()

        startWorkM()
    ////    scan.visible = false

      /*  runDelayed(4000){
        //    scan.visible = true
        }*/
    }

    private fun startWorkM() {
        val work = PeriodicWorkRequest.Builder(RefreshTokenWorker::class.java, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MINUTES, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES)
        periodicWorkRequest = work.build()

        workManager.enqueue(periodicWorkRequest)
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