package com.spacesofting.weshare.presentation.presenter

import android.annotation.SuppressLint
import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.presentation.mvp.Wish
import com.spacesofting.weshare.domain.usecases.ShowAdverShowUseCase
import com.spacesofting.weshare.presentation.view.ShowAdvertView

@InjectViewState
class ShowAdvertPresenter : MvpPresenter<ShowAdvertView>() {
    var ITEMS_PER_PAGE = 10
    var paginatePage = 0
    var lastLoadedCount = 0

    @SuppressLint("CheckResult")
    fun showAdvertFolds(idAdvert: String) {
        val useCase = ShowAdverShowUseCase(idAdvert)
        useCase.execute(profile)
            ?.subscribe({ advert ->
                viewState.setAdvertFold(advert)
            }, { _ ->
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
