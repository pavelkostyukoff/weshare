package com.spacesofting.weshare.mvp.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.mvp.presentation.view.IShootItView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class IShootItPresenter : MvpPresenter<IShootItView>() {

    fun loadMyGoods() {
        Api.Adverts.getMyAdverts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({

            }){

            }

    }

}
