package com.spacesofting.weshare.mvp.presentation.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class, tag = "")

interface AddressSearchView : MvpView {


}
