package com.spacesofting.weshare.mvp.presentation.presenter

import android.annotation.SuppressLint
import com.spacesofting.weshare.api.Api
import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.mvp.presentation.view.MapViewMaps
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MapPresenter : MvpPresenter<MapViewMaps>() {
    val ITEMS_PER_PAGE = 20
    val ITEMS_PER_PAGE_WISH_LIST = 5
    var page = 0
    var lastLoadedCount = 0
    var paginateLoading = false

    fun getCategoryAdvertsList(id: String?) {

    }

    @SuppressLint("CheckResult")
    fun getFirstRequest(
        lat: String,
        lan: String,
        sRadius: String,
        categoty: String
    ) {
        Api.Adverts.getMyAdverts(lan,lat,sRadius,categoty)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewState.setFirstRequest(it)

            }){
                it
            }
       /* Api.Tags.getCategories("00000000-0000-0000-0000-000000000000" ,ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)//одежда clothes
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ ent ->
                ApplicationWrapper.category?.entities?.clear()
                ApplicationWrapper.category = ent
                //  ent.entities?.let { checkFavoritCompilations(it) }
             //   ent.entities?.let { checkFavoritCompilations(it) }?.let { viewState.onLoadCompilations(it) }
              //  viewState.setProgressAnimation(false)
            }, { error ->
                viewState.onLoadCompilations(ArrayList())
                viewState.setProgressAnimation(false)
            })*/
    }

}
