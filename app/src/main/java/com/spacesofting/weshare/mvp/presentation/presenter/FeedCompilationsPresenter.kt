package com.spacesofting.weshare.mvp.presentation.presenter

import android.annotation.SuppressLint
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.Entity
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.DatumRequast
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.model.dto.WishList
import com.spacesofting.weshare.mvp.presentation.usecases.GetCategoriesUseCase
import com.spacesofting.weshare.mvp.presentation.usecases.GetOneCategoryUseCase
import com.spacesofting.weshare.mvp.presentation.view.FeedCompilationsView
import com.spacesofting.weshare.mvp.ui.adapter.FeedCompilationsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class FeedCompilationsPresenter : MvpPresenter<FeedCompilationsView>() {
        var user :User? = null
        var default: WishList? = null
        var arrTest = ArrayList<String>()
        val ITEMS_PER_PAGE = 20
        val ITEMS_PER_PAGE_WISH_LIST = 5
        var page = 0
        var lastLoadedCount = 0
        var useCaseGetCategory = GetCategoriesUseCase()
        var useCaseGetOneCategory = GetOneCategoryUseCase()
        var item8Maps = HashMap<String,ArrayList<DatumRequast>>()
        var paginateLoading = false
        var entitiesListPresenter: ArrayList<Entity>? = null
    init {
        user = Settings.get()
        if (user == null)
        {
            with(Api) {
                Users.getAccount()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ profile ->
                            Settings.set(profile)
                        }, { e ->
                        })
            }
        }
    }


    fun getMay8Adverts() {
        //todo сюда получаем список категорий
        //todo вызываем из него map
        //todo для каждой итерации выбираем категорию и делаем запрос на сервер для получения списка 8 вещей по каждой
        //todo при получении заносим в Map ключ - это категория - значение - список из 8 вещей
        //todo после обработке отдаем UI
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

        @SuppressLint("CheckResult")
        fun loadCompilations() {
            useCaseGetCategory.getCategory("00000000-0000-0000-0000-000000000000" ,ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)
                ?.subscribe({ ent ->
                    ApplicationWrapper.category?.entities?.clear()
                    ApplicationWrapper.category = ent
                  //  ent.entities?.let { checkFavoritCompilations(it) }
                    entitiesListPresenter = ent.entities
                    ent.entities?.let { checkFavoritCompilations(it) }?.let { viewState.onLoadCompilations(it) }
                    ent.entities?.map {
                        getOneCategory(it.id)
                        arrTest.add(it.id.toString())
                    }
                    viewState.setProgressAnimation(false)

                }, { error ->
                    viewState.onLoadCompilations(ArrayList())
                    viewState.setProgressAnimation(false)
                })

        /*    ApplicationWrapper.category?.entities?.map {
                getOneCategory(it.id)
                arrTest.add(it.id.toString())
            }*/
      /*    ApplicationWrapper.category?.entities?.map {entity->

            }*/
        }
/*    val observableOne = Observable.just(Api.Tags.get8ItemToFeed(59.956.toString(), 30.313.toString(), 500000.toString(),8, ApplicationWrapper.category?.entities?.get(0)?.name.toString()))
    val observableTwo = Observable.just(Api.Tags.get8ItemToFeed(59.956.toString(), 30.313.toString(), 500000.toString(),8, ApplicationWrapper.category?.entities?.get(0)?.name.toString()))
    val observableThree = Observable.just(Api.Tags.get8ItemToFeed(59.956.toString(), 30.313.toString(), 500000.toString(),8, ApplicationWrapper.category?.entities?.get(0)?.name.toString()))
    val observableFour = Observable.just(Api.Tags.get8ItemToFeed(59.956.toString(), 30.313.toString(), 500000.toString(),8, ApplicationWrapper.category?.entities?.get(0)?.name.toString()))
    val observableFive= Observable.just(Api.Tags.get8ItemToFeed(59.956.toString(), 30.313.toString(), 500000.toString(),8, ApplicationWrapper.category?.entities?.get(0)?.name.toString()))
    val observableSix = Observable.just(Api.Tags.get8ItemToFeed(59.956.toString(), 30.313.toString(), 500000.toString(),8, ApplicationWrapper.category?.entities?.get(0)?.name.toString()))
val    val observableSeven = Observable.just("Bye", "Friends")
    val observableEgth = Observable.just("Bye", "Friends")
    val observableNine = Observable.just("Bye", "Friends")
    val observableThen = Observable.just("Bye", "Friends")
    val zipper = BiFunction<String, String, String> { first, second -> "$first - $second" }
    val test = Observable.zip(observableOne, observableTwo, zipper)
    .subscribe {
        println(it)

    }*/

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

        fun showWish(wish: DatumRequast, compilation: Entity) {
            viewState.goToWish(wish, compilation)
        }

    @SuppressLint("CheckResult")
    private fun getOneCategory(id: String?) {
        useCaseGetOneCategory.getCategory(59.956.toString(), 30.313.toString(), 50000000.toString(),8, id.toString())
            ?.subscribe({ ent ->
                //todo set adapter?
                    item8Maps.put(id.toString(),ent.data!!)
                    viewState.refreshAdapter()
             //   arrTest.zip(ent.data!!)
                viewState.setProgressAnimation(false)
            }, { error ->
                viewState.onLoadCompilations(ArrayList())
                viewState.setProgressAnimation(false)
            })
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