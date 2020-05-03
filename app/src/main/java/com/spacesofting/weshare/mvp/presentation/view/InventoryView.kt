package com.spacesofting.weshare.mvp.presentation.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class, tag = "")

interface InventoryView : MvpView {

}