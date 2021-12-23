package com.spacesofting.weshare.domain.common

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.Gravity
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import moxy.MvpAppCompatActivity
import com.spacesofting.weshare.R
import com.spacesofting.weshare.ui.fragment.InventoryFragment
import com.spacesofting.weshare.domain.common.utils.inflate
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_wrapper.*

open class ActivityWrapper : MvpAppCompatActivity() {
    enum class HomeAsUpIndicatorType {
        HAMBURGER,
        BACK_ARROW,
        NONE
    }

    lateinit var router: BoomerangoRouter
    private val MAPKIT_API_KEYMAPKIT_API_KEY = "42e20f72-1a03-4a0d-9a60-155947e01546"

    companion object {
        var serverNumber = ""
        var idMyTask = ""
        var myWorkSessions = ""
        var userSessionId = ""
        var nameUser = ""
        var soNameUser = ""
        var fragmnetTag: String? = ""
        var middleName = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_wrapper)
        //  Fabric.with(this, Crashlytics())
        setSupportActionBar(mainToolbar)
        router = ApplicationWrapper.instance.getRouter()
        showToolbar(false)
        lockDrawerMenu(true)
        scan.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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
        drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        when (indicatorType) {
            HomeAsUpIndicatorType.HAMBURGER -> {
                drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)
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
            drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)
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

    val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_feed -> {
                    //   if (Settings.IsAuthorized) {
                    router.replaceScreen(ScreenPool.FEED_FRAGMENT)
                    /*    } else {
                        toast(R.string.rega)
                        }*/
                    //todo запрос актуальных задачь - положить их в список и открыть
                    /*   if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                          // getMyTask()
                       } else {
                           Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                       }*/
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_map -> {
                    this.let {
                        RxPermissions(it)
                            .request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                            .subscribe { granted ->
                                if (granted) {
                                    router.replaceScreen(ScreenPool.MAP_FRAGMENT)
                                } else {
                                    //todo запрос на пермишн
                                }
                            }
                    }
                    //  if (Settings.IsAuthorized) {
                    /*   } else {
                           toast(R.string.rega)
                       }*/


                    //  when(fragmnetTag) {

/*
                    "WSTFR" -> {
                        if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                            showPopupMenu(scan)
                        } else {
                            Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    "SIMPLJOB" -> {
                        if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                            showPopupMenuSimpleJob(scan)
                        } else {
                            Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "NO_PRESS" -> {
                        if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                        } else {
                            Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                        }
                    }*/

                    //      else -> Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                    //    }

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_inventory -> {
                    //   if (Settings.IsAuthorized) {
                    //   router.replaceScreen(ScreenPool.INVENTORY_FRAGMENT,0)
                    router.replaceScreen(
                        ScreenPool.INVENTORY_FRAGMENT,
                        InventoryFragment.getBundle(0, null)
                    )

                    /*  } else {
                          toast(R.string.rega)
                      }*/
                    /*  if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                          allMyTask = TaskFragment.taskList
                          if (allMyTask.isNotEmpty()) {
                              router.newScreenChain(ScreenPool.BMTCWWCF)
                          } else {
                              Toast.makeText(
                                  this, "У Вас не текущих задач.",
                                  Toast.LENGTH_SHORT
                              ).show()
                          }*/
/*
                } else {
                    Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                }*/
                    return@OnNavigationItemSelectedListener true
                }
            }
            true
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

    override fun onBackPressed() {
        val count = getSupportFragmentManager().getBackStackEntryCount()

        if (count == 0) {
            showLogoutDialogClosed()
        } else {
            getSupportFragmentManager().popBackStack()
        }
    }

    fun showLogoutDialogClosed() {
        MaterialDialog.Builder(this)
            .title("Вы действительно хотите выйти из приложения?")
            .cancelable(true)
            .negativeText("Отмена")
            .positiveText("Выйти")
            .onPositive { _, _ ->
                super.onBackPressed()
            }
            .onNegative { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}