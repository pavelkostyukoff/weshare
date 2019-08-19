package com.spacesofting.weshare.mvp.view

import com.arellomobile.mvp.MvpView
import com.spacesofting.weshare.mvp.Category
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Datum
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.ui.adapter.FeedCompilationsAdapter

interface FeedCompilationsView : MvpView {
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
