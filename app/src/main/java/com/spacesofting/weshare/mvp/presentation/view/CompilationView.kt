package com.spacesofting.weshare.mvp.presentation.view

import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.ui.adapter.ListWishElement
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface CompilationView : MvpView {
    fun addProgress(item: ListWishElement, isActive: Boolean)
    fun wishAdded(wish: Wish)
    fun loadWish(list: List<Wish>)
    fun onSubscribe(compilationId: Int)
    fun onUnsubscribe(compilationId: Int)
    fun hideAddAnimation(wish: Wish)
    fun onErrorAdded(wish: Wish)
    fun onAdded(wish: Wish)
    fun goToWish(wish: Wish, compilation: Compilation)
}
