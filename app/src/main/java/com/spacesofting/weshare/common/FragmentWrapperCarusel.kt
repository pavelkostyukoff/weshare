package com.spacesofting.weshare.common

import android.support.v7.app.AlertDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.pawegio.kandroid.toast
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.inflate
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_wrapper.*
import kotlinx.android.synthetic.main.view_drawer_menu.*

abstract class FragmentWrapperCarusel : MvpAppCompatFragment() {
    companion object {
        var isInvitedMode = false
    }

    val router = ApplicationWrapper.INSTANCE.getRouter()
    val TOOLBAR_HIDE = 0
    val TOOLBAR_OVERLAY = 1
    val TOOLBAR_EMBEDDED = 2

    val TOOLBAR_INDICATOR_HIDE = 0
    val TOOLBAR_INDICATOR_HAMBURGER = 1
    val TOOLBAR_INDICATOR_BACK_ARROW = 2

    private var currentToolbarType = TOOLBAR_HIDE
    private var dialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wrapper, container, false)
        val viewContainer = view.findViewById<FrameLayout>(R.id.mainContainer)
        setLayout(getFragmentLayout(), viewContainer)
       // scan.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
       // scan.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        return view
    }
     val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_feed -> {
                if (Settings.IsAuthorized) {
                    router.navigateTo(ScreenPool.FEED_FRAGMENT)
                } else {
toast(R.string.rega)
                }


                //todo запрос актуальных задачь - положить их в список и открыть
             /*   if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                   // getMyTask()
                } else {
                    Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                }*/
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {

                if (Settings.IsAuthorized) {
                    router.navigateTo(ScreenPool.MAP_FRAGMENT)
                } else {
                    toast(R.string.rega)
                }


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
                if (Settings.IsAuthorized) {
                    router.navigateTo(ScreenPool.INVENTORY_FRAGMENT,1)
                } else {
                    toast(R.string.rega)
                }
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
        false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // ImageUtils.saveBaseImageUrl()
        getPriorities()

        (activity as? ActivityWrapper)?.lockDrawerMenu(true)
        showToolbar(TOOLBAR_EMBEDDED)
    }

    abstract fun getFragmentLayout(): Int
   /* val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_feed -> {
                router.navigateTo(ScreenPool.FEED_FRAGMENT)


                //todo запрос актуальных задачь - положить их в список и открыть
                *//*   if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                      // getMyTask()
                   } else {
                       Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                   }*//*
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {


                router.navigateTo(ScreenPool.MAP_FRAGMENT)


                //  when(fragmnetTag) {

*//*
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
                    }*//*

                //      else -> Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                //    }

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_inventory -> {
                router.navigateTo(ScreenPool.INVENTORY_FRAGMENT,1)
                *//*  if (com.kargo.scaner.utils.Settings.isAnonymousUser == true) {
                      allMyTask = TaskFragment.taskList
                      if (allMyTask.isNotEmpty()) {
                          router.newScreenChain(ScreenPool.BMTCWWCF)
                      } else {
                          Toast.makeText(
                              this, "У Вас не текущих задач.",
                              Toast.LENGTH_SHORT
                          ).show()
                      }*//*
*//*
                } else {
                    Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show()
                }*//*
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }*/
    fun setTitle(str: String) {
      /*  val title = getToolbarContainer().findViewById<TextView>(R.id.toolbarTitle)
        title?.text = str*/
    }

    fun setTitleColor(colorId: Int) {
    /*  //  val title = getToolbarContainer().findViewById<TextView>(R.id.toolbarTitle)
        context?.let {
            title?.setTextColor(ContextCompat.getColor(it, colorId))
        }*/
    }

    /**
     * @param toolbarType chose type of toolbar
     * @param resId add your custom toolbar resource
     */
    fun showToolbar(toolbarType: Int, resId: Int = -1) {
        val layoutRes = if (resId == -1) R.layout.view_fragment_toolbar else resId
        embeddedToolbarContainer.removeAllViews()
        overlayToolbarContainer.removeAllViews()
        currentToolbarType = toolbarType

        when (toolbarType) {
            TOOLBAR_EMBEDDED -> {
                setLayout(layoutRes, embeddedToolbarContainer)
            }
            TOOLBAR_OVERLAY -> {
                setLayout(layoutRes, overlayToolbarContainer)
            }
            TOOLBAR_HIDE -> {
            }
        }
    }

    /**
     * this functions:
     * - setToolbarBackgroundColor
     * - setToolbarBackgroundDrawable
     * work only with embedded toolbar
     */

    fun setToolbarBackgroundColor(colorId: Int) {
        context?.let {
            embeddedToolbarContainer.setBackgroundColor(ContextCompat.getColor(it, colorId))
        }
    }

    fun setToolbarBackgroundDrawable(drawableId: Int) {
        embeddedToolbarContainer.setBackgroundResource(drawableId)
    }

    fun setHomeAsUpIndicator(indicatorType: Int, colorId: Int = R.color.colorPrimaryDark) {
        val navButton = getToolbarContainer().findViewById<ImageView>(R.id.toolbarIndicator)

        (activity as? ActivityWrapper)?.let {
            it.lockDrawerMenu(true)

            when (indicatorType) {
                TOOLBAR_INDICATOR_HAMBURGER -> {
                    (activity as? ActivityWrapper)?.lockDrawerMenu(false)
                    navButton?.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_hamburger))
                    navButton?.setColorFilter(ContextCompat.getColor(context!!, colorId), PorterDuff.Mode.SRC_IN)
                    navButton?.visible = true
                    navButton?.setOnClickListener {
                        (activity as? ActivityWrapper)?.openDrawerMenu()
                    }
                }

                TOOLBAR_INDICATOR_BACK_ARROW -> {
                    navButton?.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_chevron_back))
                    navButton?.setColorFilter(ContextCompat.getColor(context!!, colorId), PorterDuff.Mode.SRC_IN)
                    navButton?.visible = true
                    navButton?.setOnClickListener {
                        activity?.onBackPressed()
                    }
                }

                TOOLBAR_INDICATOR_HIDE -> {
                    navButton?.visible = false
                }

                else -> {
                }
            }
        }
    }

    fun getToolbarView(): ViewGroup? {
        return getToolbarContainer().findViewById(R.id.toolbarLayout)
    }

    fun loadingDialog(isVisible: Boolean, strResId: Int = R.string.dialog_progress_please_wait) {
        if (isVisible) {
            val view = LayoutInflater.from(activity).inflate(R.layout.dialog_progress, null)
            view.findViewById<TextView>(R.id.tvLoadingMsg)?.text = getText(strResId)

            dialog = activity?.let {
                AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .show()
            }
        } else {
            dialog?.dismiss()
        }
    }

    private fun setLayout(resId: Int, container: ViewGroup) {
        container.removeAllViews()
        container.addView(context?.inflate(resId, container, false))
    }

    private fun getToolbarContainer(): View {
        return when (currentToolbarType) {
            TOOLBAR_EMBEDDED -> embeddedToolbarContainer
            TOOLBAR_OVERLAY -> overlayToolbarContainer
            else -> embeddedToolbarContainer
        }
    }

    //TODO: refactoring
    private fun getPriorities() {
//        if (AccountManager.priorities == null) {
//            Api.Users.getGuestCardsPriorities()
//                    .subscribe({ priorities ->
//                        AccountManager.priorities = priorities
//                    }, {
//                        d("Get priorities")
//                    })
//        }
    }

    fun setHamburgerForRole() {
        val inflater = LayoutInflater.from(activity)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val inflatedView: View = inflater.inflate(R.layout.view_guests_items, null, false)
        inflatedView.layoutParams = params
        (activity as ActivityWrapper).navItemsLayout.addView(inflatedView)
    }
}