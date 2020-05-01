package com.spacesofting.weshare.mvp.presentation.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface InventoryView : MvpView {

    fun openCreateFragment(newId: String)

}
