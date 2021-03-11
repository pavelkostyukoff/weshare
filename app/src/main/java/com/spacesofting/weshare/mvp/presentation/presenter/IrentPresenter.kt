package com.spacesofting.weshare.mvp.presentation.presenter

import android.annotation.SuppressLint
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.DatumRequast
import com.spacesofting.weshare.mvp.model.Advert
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.mvp.presentation.view.IrentView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

@InjectViewState
class IrentPresenter : MvpPresenter<IrentView>() {
    val router = ApplicationWrapper.instance.getRouter()
    var bannerItems = ArrayList<File>()


    init {
        getMyAdvert()
    }

    @SuppressLint("CheckResult")
    fun getMyAdvert() {
        Api.Adverts.getMeAdverts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setListAdverts(it.data)
                ApplicationWrapper.instance.myImages = it.data
                //todo тут заполняем баннеры в advert

                it
            }) {
                it
            }
    }

    fun delAdvertById(itemRent: RespounceDataMyAdverts) {
            Api.Adverts.dellMeAdverts(itemRent.id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.delAdvertCOmplite()
                    it
                }) {
                    it
                }
    }

    /*private fun editAdvert(itemRent: Advert, id: String) {
            Api.Adverts.updateMyAdvertById(itemRent,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.editCOmplite()
                    it
                }) {
                    it
                }
    }*/

    //todo дозагрузить картинки и закинуть в баннерся



    fun editAdverts(itemRent: RespounceDataMyAdverts) {
        Api.Adverts.getMyAdvertById(itemRent.id!!)//todo take goods by id
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({advert->
                ApplicationWrapper.instance.setAuthorityWish(advert) //reciver
             //   itemRent.id
               // it
                //viewState.delAdvertCOmplite()
              //  editAdvert(it, itemRent.id!!)
                router.navigateTo(ScreenPool.ADD_GOODS, itemRent.id)
            }) {
                System.out.println(it)
            }
    }


}
