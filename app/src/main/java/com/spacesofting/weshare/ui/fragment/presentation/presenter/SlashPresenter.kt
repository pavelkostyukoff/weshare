package com.spacesofting.weshare.ui.fragment.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.ui.fragment.presentation.view.SlashView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

@InjectViewState
class SlashPresenter : MvpPresenter<SlashView>() {
    var wasCalled = false
    var timeout: Timer = Timer()

    fun isAuthenticated() = Settings.AccessToken != null

  /*  fun init() {
        if (!wasCalled) {
            timeout.schedule(object : TimerTask() {
                override fun run() {
                    wasCalled = true
                    if (isAuthenticated()) {
                        Api.Users.get()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ profile ->
                                ApplicationWrapper.instance.profile = profile
                                viewState.goToMainScreen()
                            }, { e ->
                                ApplicationWrapper.instance.profile = null
                                viewState.goToMainScreen()
                            })
                    } else {
                        viewState.goToNext()
                    }
                }
            }, 2000)
        }  else {
            viewState.goToNext()
        }
    }*/

    fun cancelTransition() {
        timeout.cancel()
    }
}
