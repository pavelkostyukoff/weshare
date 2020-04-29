package com.spacesofting.weshare.mvp.presentation.view

import com.arellomobile.mvp.MvpView
import com.spacesofting.weshare.api.Entity
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.ui.adapter.FeedCompilationsAdapter

interface FeedCompilationsView : MvpView {
    fun scrollOnTop()
    fun onLoadCompilations(list: List<Entity>)
    fun onSubscribe(compilationId: Int)
    fun onUnsubscribe(compilationId: Int)
    fun hideAddAnimation(wish: Wish, compilation: Entity?, adapter: FeedCompilationsAdapter? = null)
    fun setProgressAnimation(isEnable: Boolean)
    fun onAdded(wish: Wish)
    fun onErrorAdded(wish: Wish)
    fun goToCompilation(compilation: Entity)
    fun goToEditWish(wish: Wish)
    fun goToWish(wish: Wish, compilation: Entity)
}
