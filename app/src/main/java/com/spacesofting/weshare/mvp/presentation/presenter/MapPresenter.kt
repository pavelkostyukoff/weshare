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
    fun getNewMapRequest(
        lat: String,
        lan: String,
        searchRadius: String,
        category: String
    ) {
        Api.Adverts.getMyAdverts(lan, lat, searchRadius, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setUpdateRequest(it)
            }) {
                //Log.e(it,"")
            }
    }
}
