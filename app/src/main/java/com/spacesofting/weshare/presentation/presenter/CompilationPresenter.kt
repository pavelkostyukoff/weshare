package com.spacesofting.weshare.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.presentation.mvp.Compilation
import com.spacesofting.weshare.presentation.mvp.Wish
import com.spacesofting.weshare.presentation.view.CompilationView

@InjectViewState
class CompilationPresenter : MvpPresenter<CompilationView>() {
    var ITEMS_PER_PAGE = 10
    var paginatePage = 0
    var lastLoadedCount = 0
    var compilation: Compilation? = null

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
        compilation?.let {
           /* Api.Wishes.getWishesFromCompilation(it.id, ITEMS_PER_PAGE, paginatePage * ITEMS_PER_PAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    viewState.loadWish(list)
                }, { error ->
                    viewState.loadWish(ArrayList())
                })*/
        }
    }

    fun subscribeCompilations() {
        compilation?.let {
           /* if (!Settings.isAuthenticated()) {
                Settings.subscribe(it.id)
                viewState.onSubscribe(it.id)
            } else {
                val favoriteCompilations = FavoriteCompilations()
                favoriteCompilations.idsList = hashSetOf(it.id)

                Api.Wishes.compilationsAdd(favoriteCompilations)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.onSubscribe(it.id)
                    }, { error ->
                        d("subscribeCompilation", error.message.toString())
                    })
            }*/
        }
    }

    fun unsubscribeCompilation() {
        compilation?.let {
          /*  if (!Settings.isAuthenticated()) {
                Settings.unsubscribe(it.id)
                viewState.onUnsubscribe(it.id)
            } else {
                Api.Wishes.compilationDelete(it.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.onUnsubscribe(it.id)
                    }, { error ->
                        //d("unsubscribeCompilation", error.message.toString())
                    })
            }*/
        }
    }

    fun goToWish(wish: Wish) {
        compilation?.let {
            viewState.goToWish(wish, it)
        }
    }
}
