package com.spacesofting.weshare.common

import android.content.Context
import android.graphics.Bitmap
import androidx.multidex.MultiDexApplication
import com.jakewharton.picasso.OkHttp3Downloader
import com.pawegio.kandroid.e
import com.spacesofting.weshare.api.Entitys
import com.spacesofting.weshare.api.Image
import com.spacesofting.weshare.api.model.place.Place
import com.spacesofting.weshare.di.AppComponent
/*
import com.spacesofting.weshare.di.DaggerAppComponent
*/
import com.spacesofting.weshare.mvp.Login
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.model.Advert
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import com.squareup.picasso.Picasso
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.terrakok.cicerone.Cicerone
import java.io.File


class ApplicationWrapper : MultiDexApplication() {
    private var component: AppComponent? = null

    private var doAuthtorizedWish: Advert? = null
        private var photoPath: File? = null
        var profile: User? = null
        var isDesireToAuthorize: Boolean = false
        val APPSFLYER = "a7vkdKBUKnfGBnz5BahSRd"
        val ONE_LINK_ID = "vVZC"
        var isNewUser = false
        var sharingWishID = 0
        var goodId: String? = ""
        var idForEdit: String? = null
        var myImages: ArrayList<RespounceDataMyAdverts>? = null


    companion object {
        lateinit var instance: ApplicationWrapper
        lateinit var user: User
        var place = Place()
        var advert : Advert? = null
        lateinit var context: Context
        lateinit var file: File
        var avatar: String? = null
        var editAdvertId: String? = null
        var isAddress: Boolean? = null
        var category: Entitys? = null

        lateinit var mailAutorize : Login

        //  lateinit var updProfile: UpdateProfile
    }

    fun getComponent(): AppComponent? {
        return component
    }

    private lateinit var cicerone: Cicerone<BoomerangoRouter>

    fun getAuthorityWish(): Advert? {
        return this.doAuthtorizedWish
    }

    fun getImagePathWish(): File? {
        return photoPath
    }

    fun setAuthorityWish(advert: Advert?=null , photoPath: File? = null) {
        doAuthtorizedWish = advert
        this.photoPath = photoPath
        isDesireToAuthorize = true
    }

    fun isNewUserProfile(): Boolean{
        return isNewUser
    }

    fun setNewUserFlag(isNew: Boolean){
        isNewUser = isNew
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
/*
        component = DaggerAppComponent.create()
*/
        user = User()
        initCicerone()

        RxJavaPlugins.setErrorHandler { e("onError not implemented! ${it.message}") }

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
        val picasso = Picasso.Builder(this)
            .defaultBitmapConfig(Bitmap.Config.RGB_565)
            .downloader(OkHttp3Downloader(client))
            .build()
        Picasso.setSingletonInstance(picasso)
    }

    private fun initCicerone() {
        cicerone = Cicerone.create(BoomerangoRouter())
    }

    fun getNavigationHolder() = cicerone.navigatorHolder
    fun getRouter() = cicerone.router

    fun restartApp() {
        System.exit(0)
    }
}