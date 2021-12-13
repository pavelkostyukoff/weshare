package com.spacesofting.weshare.presentation.view

import com.spacesofting.weshare.data.api.ResponceMyAdvertMaps
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface MapViewMaps : MvpView {

    fun showCatObjects(it: ResponceMyAdvertMaps)
    fun setUpdateRequest(it: ResponceMyAdvertMaps)

}
