package com.spacesofting.weshare.presentation.view

import com.spacesofting.weshare.presentation.mvp.Wish
import com.spacesofting.weshare.presentation.mvp.model.dto.AdvertModifiredRespouns
import com.spacesofting.weshare.presentation.ui.adapter.ListWishElement
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface ShowAdvertView : MvpView {
    fun addProgress(item: ListWishElement, isActive: Boolean)
    fun wishAdded(wish: Wish)
    fun setAdvertFold(advertRespouns: AdvertModifiredRespouns)
    fun loadWish(list: List<Wish>)
    fun hideAddAnimation(wish: Wish)
    fun onErrorAdded(wish: Wish)
    fun onAdded(wish: Wish)
}
