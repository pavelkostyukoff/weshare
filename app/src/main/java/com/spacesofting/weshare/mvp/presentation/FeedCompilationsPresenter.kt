package com.spacesofting.weshare.mvp.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.pawegio.kandroid.d
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.model.dto.WishList
import com.spacesofting.weshare.mvp.view.FeedCompilationsView
import com.spacesofting.weshare.ui.adapter.FeedCompilationsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.ArrayList
import java.util.HashSet

@InjectViewState
class FeedCompilationsPresenter : MvpPresenter<FeedCompilationsView>() {
        var default: WishList? = null
        val ITEMS_PER_PAGE = 5
        val ITEMS_PER_PAGE_WISH_LIST = 10
        var page = 0
        var lastLoadedCount = 0
        var paginateLoading = false

        fun addWish(wish: Wish, compilation: Compilation?, adapter: FeedCompilationsAdapter? = null) {
         /*   Api.Wishes.wishAdd(wish)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wishNew ->
                    viewState.hideAddAnimation(wish, compilation, adapter)
                    viewState.onAdded(wish)
                }, { error ->
                    viewState.hideAddAnimation(wish, compilation, adapter)
                })*/
        }

        fun loadCompilations() {
          /*  Api.Wishes.getListCompilations(ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    viewState.onLoadCompilations(checkFavoritCompilations(list))
                    viewState.setProgressAnimation(false)
                }, { error ->
                    viewState.onLoadCompilations(ArrayList())
                    viewState.setProgressAnimation(false)
                })*/
        }

        fun loadCompilationsWishes(compilation: Compilation, success: (List<Wish>) -> Unit, failure: (error: Throwable) -> Unit) {
           /* Api.Wishes.getWishesFromCompilation(compilation.id, ITEMS_PER_PAGE_WISH_LIST, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list -> success(list) },
                    { error -> failure(error) }
                )*/
        }

        fun subscribeCompilations(idsList: HashSet<Int>) {
          /*  if(idsList.isEmpty()){
                return
            }

            val fCompilations = FavoriteCompilations()
            fCompilations.idsList.addAll(idsList)

            if (ApplicationWrapper.instance.profile == null) {
                idsList.forEach{
                    Settings.subscribe(it)
                    viewState.onSubscribe(it)
                }
            } else {
                Api.Wishes.compilationsAdd(fCompilations)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        idsList.forEach {
                            viewState.onSubscribe(it)
                            Settings.unsubscribe(it)
                        }
                    }, { error ->
                        d("subscribeCompilation", error.message.toString())
                    })
            }*/
        }

        fun unsubscribeCompilation(compilation: Compilation) {
           /* if (ApplicationWrapper.instance.profile == null) {
                Settings.unsubscribe(compilation.id)
                viewState.onUnsubscribe(compilation.id)
                return
            } else {
                Api.Wishes.compilationDelete(compilation.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.onUnsubscribe(compilation.id)
                    })
            }*/
        }

        fun showCompilationDetails(compilation: Compilation) {
            viewState.goToCompilation(compilation)
        }

        fun showWish(wish: Wish, compilation: Compilation) {
            viewState.goToWish(wish, compilation)
        }

        //todo refactoring
        private fun checkFavoritCompilations(list: List<Compilation>): List<Compilation> {
            val compilationList = list
           // val idsList = Settings.getListInt()

        /*    if (idsList.isNotEmpty()) {
                compilationList.forEach {
                    val compilation = it

                  *//*  idsList.forEach {
                        if (compilation.id == it) {
                            compilation.isFavorite = true
                        }
                    }*//*
                }
            }*/
            return compilationList
        }
    }