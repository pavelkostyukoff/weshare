package com.spacesofting.weshare.presentation.view

import com.spacesofting.weshare.presentation.mvp.Template
import com.spacesofting.weshare.presentation.mvp.Wish
import com.spacesofting.weshare.presentation.ui.adapter.ListWishElement
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class, tag = "")

interface FeedView : MvpView {
    fun setLoadingProgressVisibility(visible: Boolean)
    fun showWishAddingProgress(item: ListWishElement?)
    fun hideWishAddingProgress(sourceItemId: Int)
    fun showLoadingError(errorMessage: String?)
    fun showLoadingError(errorMessageRes: Int)
    fun showPageLoadingError()
    fun hidePageLoadingError()
    fun addTemplateWishes(items: List<ListWishElement>)
    fun wishAdded(wish: Wish, sourceId: Int)
    fun wishExists(item: Template, count: Int)
    fun wishAddError(errorMessage: String?)
    fun goToEditProfile()
    fun editWish(wish: Wish)
    fun goToProfile(id: Int)
    fun goToWish(wish: ListWishElement)

    fun reload()

    fun pay(wish: Wish)
    fun goToRegister()
    fun scrollOnTop()
}
