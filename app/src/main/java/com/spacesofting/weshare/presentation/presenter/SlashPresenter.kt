package com.spacesofting.weshare.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.domain.common.Settings
import com.spacesofting.weshare.presentation.view.SlashView
import java.util.*

@InjectViewState
class SlashPresenter : MvpPresenter<SlashView>() {
    var wasCalled = false
    var timeout: Timer = Timer()

    fun isAuthenticated() = Settings.accessToken != null

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
