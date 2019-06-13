package com.spacesofting.weshare.common

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.spacesofting.weshare.utils.inflate
import com.spacesofting.weshare.R
import kotlinx.android.synthetic.main.activity_wrapper.*

open class ActivityWrapper : MvpAppCompatActivity() {
    enum class HomeAsUpIndicatorType {
        HAMBURGER,
        BACK_ARROW,
        NONE
    }
    lateinit var router: Boomerango

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_wrapper)
      //  Fabric.with(this, Crashlytics())
        setSupportActionBar(mainToolbar)
        router = ApplicationWrapper.INSTANCE.getRouter()
        showToolbar(false)
        lockDrawerMenu(true)
    }

    override fun setContentView(view: View?) {
        mainContainer.removeAllViews()
        mainContainer.addView(view)
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(inflate(layoutResID, mainContainer, false))
    }

    fun showToolbar(isVisible: Boolean) {
        if (isVisible) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    fun setHomeAsUpIndicator(indicatorType: HomeAsUpIndicatorType) {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        when (indicatorType) {
            HomeAsUpIndicatorType.HAMBURGER -> {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeAsUpIndicator(R.drawable.ic_hamburger)
                }
                mainToolbar.setNavigationOnClickListener {
                    drawerLayout.openDrawer(Gravity.START)
                }
            }

            HomeAsUpIndicatorType.BACK_ARROW -> {
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeAsUpIndicator(R.drawable.ic_chevron_back)
                }
                mainToolbar.setNavigationOnClickListener {
                    onBackPressed()
                }
            }

            HomeAsUpIndicatorType.NONE -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    fun lockDrawerMenu(isLocked: Boolean) {
        if (isLocked) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    fun openDrawerMenu() {
        drawerLayout.openDrawer(Gravity.START)
    }

    fun closeDrawerMenu() {
        drawerLayout.closeDrawer(Gravity.END)
    }

    fun onClickItemMenu(view: View) {
        when (view.id) {
            R.id.navItemLogOut -> {
                showLogoutDialog()
            }
        }
    }

    fun showLogoutDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.logout_going_exit_from_application)
                .cancelable(true)
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .onPositive { _, _ ->
                    logout()
                }
                .onNegative { dialog, _ ->
                    dialog.cancel()
                }
                .show()
    }

    fun logout() {
    /*    Api.Auth.logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Settings.logout()
                    mainUserName.visible = false
                    mainAvatar.visible = false
                    mainUserName.setText(R.string.hamburger_default_username)
                    mainAvatar.setImageResource(R.color.colorPrimaryLight)
                    navItemsLayout.removeAllViews()
                    router.newRootScreen(ScreenPool.REGISTRATION_FRAGMENT)
                }, { e ->
                    Toast.makeText(this, R.string.error_general, Toast.LENGTH_LONG).show()
                })*/
    }
}