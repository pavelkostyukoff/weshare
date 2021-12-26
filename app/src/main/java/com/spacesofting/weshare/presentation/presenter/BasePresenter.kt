package com.spacesofting.weshare.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.presentation.view.BaseView
import org.koin.core.KoinComponent

@InjectViewState
class BasePresenter : MvpPresenter<BaseView>(), KoinComponent {

}
