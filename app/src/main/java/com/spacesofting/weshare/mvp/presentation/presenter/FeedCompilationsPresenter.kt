package com.spacesofting.weshare.mvp.presentation.presenter

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.Entity
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.model.dto.WishList
import com.spacesofting.weshare.mvp.presentation.view.FeedCompilationsView
import com.spacesofting.weshare.mvp.ui.adapter.FeedCompilationsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*

@InjectViewState
class FeedCompilationsPresenter : MvpPresenter<FeedCompilationsView>() {
        var default: WishList? = null
        val ITEMS_PER_PAGE = 20
        val ITEMS_PER_PAGE_WISH_LIST = 5
        var page = 0
        var lastLoadedCount = 0
        var paginateLoading = false

    init {
        if (ApplicationWrapper.instance.profile == null)
        {
            with(Api) {
                Users.getAccount()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ profile ->
                            ApplicationWrapper.instance.profile = profile
                        }, { e ->
                        })
            }
        }
    }

        fun addWish(wish: Wish, compilation: Entity?, adapter: FeedCompilationsAdapter? = null) {
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
            Api.Tags.getCategories("00000000-0000-0000-0000-000000000000" ,ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)//одежда clothes
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ent ->
                    ApplicationWrapper.category?.entities?.clear()
                    ApplicationWrapper.category = ent
                  //  ent.entities?.let { checkFavoritCompilations(it) }
                    ent.entities?.let { checkFavoritCompilations(it) }?.let { viewState.onLoadCompilations(it) }
                    viewState.setProgressAnimation(false)
                }, { error ->
                    viewState.onLoadCompilations(ArrayList())
                    viewState.setProgressAnimation(false)
                })
        }

        fun loadCompilationsWishes(compilation: Entity, success: (List<Wish>) -> Unit, failure: (error: Throwable) -> Unit) {
            //todo а тут мы грузим все вещи по данной категории или тегу ну или имитируем
          /*  Api.Wishes.getWishesFromCompilation(compilation.id, ITEMS_PER_PAGE_WISH_LIST, 0)
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

        fun showCompilationDetails(compilation: Entity) {
            //todo переход в категорию передача  id категории
            viewState.goToCompilation(compilation)
        }

        fun showWish(wish: Wish, compilation: Entity) {
            viewState.goToWish(wish, compilation)
        }

        //todo refactoring
        private fun checkFavoritCompilations(list: List<Entity>): List<Entity> {
            val compilationList = list
            val idsList = Settings.getListInt()

            if (idsList.isNotEmpty()) {
                compilationList.forEach {
                    val compilation = it

                    idsList.forEach {
                        if (compilation.id?.equals(it) ?: (it == null)) {
                           // compilation.isFavorite = true
                        }
                    }
                }
            }
            return compilationList
        }
    }