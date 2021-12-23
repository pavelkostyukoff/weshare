package com.spacesofting.weshare.domain.usecases

import android.annotation.SuppressLint
import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.data.api.Entitys
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetSubCategoryUseCase {

    @SuppressLint("CheckResult")
    fun getSubCat(id: String?, ITEMS_PER_PAGE: Int, page: Int): Observable<Entitys>? {

        // вот это поменять на корутину
        return Api.Tags.getCategories(id, ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}