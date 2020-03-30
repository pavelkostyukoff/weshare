package com.spacesofting.weshare.mvp.presentation.views.splashbackgroundview

/**
 * Created by bender on 06/06/2017.
 */
interface SplashElement {

    fun appear(startOffset: Long)
    fun appearPartly(startOffset: Long)
    fun introduceElement()
    fun disposeElement()
    fun disappear(startOffset: Long)

}