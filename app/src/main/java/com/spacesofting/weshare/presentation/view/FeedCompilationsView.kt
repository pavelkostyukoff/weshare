package com.spacesofting.weshare.presentation.view

import com.spacesofting.weshare.data.api.Entity
import com.spacesofting.weshare.mvp.DatumRequast
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.ui.adapter.FeedCompilationsAdapter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "Cheese")

interface FeedCompilationsView : MvpView {
    fun scrollOnTop()
    fun onLoadCompilations(list: List<Entity>)
    fun onSubscribe(compilationId: Int)
    fun refreshAdapter()
    fun onUnsubscribe(compilationId: Int)
    fun hideAddAnimation(wish: Wish, compilation: Entity?, adapter: FeedCompilationsAdapter? = null)
    fun setProgressAnimation(isEnable: Boolean)
    fun onAdded(wish: Wish)
    fun onErrorAdded(wish: Wish)
    fun goToCompilation(compilation: Entity)
    fun goToEditWish(wish: Wish)
    fun goToWish(wish: DatumRequast, compilation: Entity)
}
