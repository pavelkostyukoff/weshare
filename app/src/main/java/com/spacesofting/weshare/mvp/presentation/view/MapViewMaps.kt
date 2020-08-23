package com.spacesofting.weshare.mvp.presentation.view

import com.spacesofting.weshare.api.ResponceMyAdvert
import com.spacesofting.weshare.api.ResponceMyAdvertMaps
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface MapViewMaps : MvpView {

    fun showCatObjects(it: ResponceMyAdvertMaps)
    fun setFirstRequest(it: ResponceMyAdvertMaps)

}
