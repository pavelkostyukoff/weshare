package com.spacesofting.weshare.presentation.presenter

import android.annotation.SuppressLint
import com.spacesofting.weshare.domain.usecases.GetMyAdvertToMapsUseCase
import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.presentation.view.MapViewMaps

@InjectViewState
class MapPresenter : MvpPresenter<MapViewMaps>() {
    val ITEMS_PER_PAGE = 20
    val ITEMS_PER_PAGE_WISH_LIST = 5
    var page = 0
    var lastLoadedCount = 0
    var paginateLoading = false
    var useCase = GetMyAdvertToMapsUseCase()
    fun getCategoryAdvertsList(id: String?) {

    }

    @SuppressLint("CheckResult")
    fun getNewMapRequest(
        lat: String,
        lan: String,
        searchRadius: String,
        category: String
    ) {
            useCase.execute(lan, lat, searchRadius, category)
            ?.subscribe({
                viewState.setUpdateRequest(it)
            }) {
                //Log.e(it,"")
            }
    }
}
