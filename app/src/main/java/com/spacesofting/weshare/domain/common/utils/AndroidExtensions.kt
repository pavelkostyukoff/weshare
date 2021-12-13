package com.spacesofting.weshare.domain.common.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.util.DisplayMetrics as DisplayMetrics1

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

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun AppCompatActivity.showKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

val AppCompatActivity.screenMetrics: DisplayMetrics1
    get() {
        val metrics = DisplayMetrics1()
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

val AppCompatActivity.screenWidth: Int
    get() = screenMetrics.widthPixels

val AppCompatActivity.screenHeight: Int
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
val Activity.screenMetrics: android.util.DisplayMetrics
    get() {
        val metrics = android.util.DisplayMetrics()
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }
val Activity.screenWidth: Int
    get() = screenMetrics.widthPixels

val Activity.screenHeight: Int
    get() = screenMetrics.heightPixels

inline fun View.afterMeasured(crossinline action: (view: View) -> Unit) {
    val oldViewTreeObserver = viewTreeObserver
    oldViewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                action(this@afterMeasured)
                disposeOnGlobalLayoutListener(oldViewTreeObserver, viewTreeObserver, this)
            }
        }
    })
}

@PublishedApi
internal fun disposeOnGlobalLayoutListener(
    oldViewTreeObserver: ViewTreeObserver,
    viewTreeObserver: ViewTreeObserver,
    listener: ViewTreeObserver.OnGlobalLayoutListener
) {
    when {
        oldViewTreeObserver.isAlive -> oldViewTreeObserver.removeOnGlobalLayoutListener(listener)
        else -> viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}


