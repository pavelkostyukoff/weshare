package com.spacesofting.weshare.mvp.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.presentation.view.SlashView
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
