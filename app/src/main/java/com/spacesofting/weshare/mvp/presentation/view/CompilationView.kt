package com.spacesofting.weshare.mvp.presentation.view

import com.arellomobile.mvp.MvpView
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.ui.adapter.ListWishElement

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
