package com.digitalhorizon.eve.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by bender on 05/06/2017.
 */

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
}

fun View.setWidth(width: Int) {
    val params = layoutParams
    params.width = width
    layoutParams = params
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.alphaAnimation(duration: Long, visibility: Int) {
    val alphaAnimation: AlphaAnimation = if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
    alphaAnimation.duration = duration
    alphaAnimation.fillAfter = true
    this.startAnimation(alphaAnimation)
}

fun Context.inflate(res: Int, parent: ViewGroup? = null, attachToRoot: Boolean = true): View {
    return LayoutInflater.from(this).inflate(res, parent, attachToRoot)
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

val Activity.screenMetrics: DisplayMetrics
    get() {
        val metrics = DisplayMetrics()
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

val Activity.screenWidth: Int
    get() = screenMetrics.widthPixels

val Activity.screenHeight: Int
    get() = screenMetrics.heightPixels

fun fromHtml(html: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(html)
    }
}

fun <T> Observable<T>.default(): Observable<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}



