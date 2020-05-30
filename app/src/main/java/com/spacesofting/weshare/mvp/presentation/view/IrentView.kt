package com.spacesofting.weshare.mvp.presentation.view

import com.spacesofting.weshare.api.ResponcePublish
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")


interface IrentView : MvpView {
    fun setListAdverts(it: ArrayList<RespounceDataMyAdverts>?)
    fun delAdvertCOmplite()
    fun editCOmplite()
}
