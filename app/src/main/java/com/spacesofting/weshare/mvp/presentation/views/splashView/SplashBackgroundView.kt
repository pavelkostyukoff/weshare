package com.spacesofting.weshare.mvp.presentation.views.splashbackgroundview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.AbsListView
import android.widget.ListView
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.runOnUiThread
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * Created by bender on 02/06/2017.
 */
class SplashBackgroundView: ListView {

    val listScrollInitDelay = 8000L
    val listScrollSpeedFactor = 10000L // less means faster
    val listInitAnimationDuration = 5000L
    val listDimmingSwitchRate = 5000L // once in a while will switch dimmed elements, less means more often

    var scrollingTimer: Timer? = null
    var dimmingTimer: Timer? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    fun init() {
        divider = null
        isVerticalScrollBarEnabled = false
        adapter = SplashBackgroundAdapter()

        setOnScrollListener(object : OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    smoothScrollToPosition(0)
                }
            }
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) { }
        })
    }

    fun startScrollingAnimation(firstStart: Boolean = true) {
        var initScrollingDelay = listScrollInitDelay
        if (!firstStart) {
            initScrollingDelay = 0
        }
        scrollingTimer = fixedRateTimer(initialDelay = initScrollingDelay, period = listScrollSpeedFactor,
                action = { runOnUiThread { scrollListBy(1) } })
        runDelayed(listInitAnimationDuration) { (adapter as SplashBackgroundAdapter).stopIntroducingMagic() }
    }

    fun startDimmingAnimation() {
        dimmingTimer = fixedRateTimer(period = listDimmingSwitchRate, action = {
            runOnUiThread { (adapter as SplashBackgroundAdapter).dimBackground() }
        })
    }

    fun stopAnimation() {
        scrollingTimer?.cancel()
        scrollingTimer = null
        dimmingTimer?.cancel()
        dimmingTimer = null
    }
}