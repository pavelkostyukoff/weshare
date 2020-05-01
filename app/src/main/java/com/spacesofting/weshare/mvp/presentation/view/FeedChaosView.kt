package com.spacesofting.weshare.mvp.presentation.view

import com.spacesofting.weshare.mvp.Datum
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.ui.adapter.FeedCompilationsAdapter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "Cheese")

interface FeedChaosView : MvpView {
    fun scrollOnTop()
    fun onLoadCompilations(list: List<Datum>)
    fun onSubscribe(compilationId: Int)
    fun onUnsubscribe(compilationId: Int)
    fun hideAddAnimation(wish: Wish, compilation: Datum?, adapter: FeedCompilationsAdapter? = null)
    fun setProgressAnimation(isEnable: Boolean)
    fun onAdded(wish: Wish)
    fun onErrorAdded(wish: Wish)
    fun goToCompilation(compilation: Datum)
    fun goToEditWish(wish: Wish)
    fun goToWish(wish: Wish, compilation: Datum)
}
