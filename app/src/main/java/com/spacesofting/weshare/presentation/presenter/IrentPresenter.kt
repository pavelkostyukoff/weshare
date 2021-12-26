package com.spacesofting.weshare.presentation.presenter

import android.annotation.SuppressLint
import com.spacesofting.weshare.presentation.common.ApplicationWrapper
import com.spacesofting.weshare.presentation.common.ScreenPool
import com.spacesofting.weshare.presentation.mvp.model.RespounceDataMyAdverts
import com.spacesofting.weshare.domain.usecases.DeleteMyAdvertsByIdUseCase
import com.spacesofting.weshare.domain.usecases.EditMyAdvertsByIdUseCase
import com.spacesofting.weshare.domain.usecases.GetMyAdvertsUseCase
import moxy.InjectViewState
import moxy.MvpPresenter
import com.spacesofting.weshare.presentation.view.IrentView
import java.io.File

@InjectViewState
class IrentPresenter : MvpPresenter<IrentView>() {
    val router = ApplicationWrapper.instance.getRouter()
    var bannerItems = ArrayList<File>()
    val useCaseGet = GetMyAdvertsUseCase()
    val useCaseDelete = DeleteMyAdvertsByIdUseCase()
    val useCaseEdit = EditMyAdvertsByIdUseCase()


    init {
        getMyAdvert()
    }

    @SuppressLint("CheckResult")
    fun getMyAdvert() {
        useCaseGet.getMyAdverts()
            .map {
                it.data?.filter { advert ->
                    advert.images?.firstOrNull()?.url != null
                } ?: emptyList()

            }

            .subscribe({

                viewState.setListAdverts(it)
                ApplicationWrapper.instance.myImages = it
            }) { it


            }
    }

    @SuppressLint("CheckResult")
    fun delAdvertById(itemRent: RespounceDataMyAdverts) {
        useCaseDelete.deleteMyAdvertById(itemRent, goodImageId)

                ?.subscribe({
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



    @SuppressLint("CheckResult")
    fun editAdverts(itemRent: RespounceDataMyAdverts) {
      useCaseEdit.editMyAdvertById(itemRent)
            ?.subscribe({advert->
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
