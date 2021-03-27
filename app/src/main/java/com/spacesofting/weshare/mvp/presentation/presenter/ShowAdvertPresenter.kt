package com.spacesofting.weshare.mvp.presentation.presenter

import com.spacesofting.weshare.api.Api
import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.presentation.view.ShowAdvertView
import io.reactivex.android.schedulers.AndroidSchedulers

@InjectViewState
class ShowAdvertPresenter : MvpPresenter<ShowAdvertView>() {
    var ITEMS_PER_PAGE = 10
    var paginatePage = 0
    var lastLoadedCount = 0

    fun showAdvertFolds(idAdvert: String) {
        Api.Adverts.newgetMyAdvertById(idAdvert)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ advert ->
                viewState.setAdvertFold(advert)
            }, { error ->
                error
            })
    }
    fun addWish(wish: Wish) {
       /* Api.Wishes.wishAdd(wish)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ wishNew ->
                viewState.hideAddAnimation(wish)
                viewState.onAdded(wish)
            }, { error ->
                viewState.hideAddAnimation(wish)
                viewState.onErrorAdded(wish)
            })*/
    }

    fun loadWishes() {
    }

    fun subscribeCompilations() {
    }

    fun unsubscribeCompilation() {
    }

    fun goToWish(wish: Wish) {
    }
}
