package com.spacesofting.weshare.mvp.presentation.view

import com.spacesofting.weshare.api.Entitys
import com.spacesofting.weshare.api.ResponcePublish
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.io.File

@StateStrategyType(value = OneExecutionStateStrategy::class, tag = "")

interface AddressSearchView : MvpView {


}
