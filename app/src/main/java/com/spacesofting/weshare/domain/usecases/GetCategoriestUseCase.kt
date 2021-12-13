package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.data.api.Entitys
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class GetCategoriesUseCase {

    fun getCategory(id: String, ITEMS_PER_PAGE: Int, page: Int): Observable<Entitys>? {

        // вот это поменять на корутину
        return   Api.Tags.getCategories(id,ITEMS_PER_PAGE, page * ITEMS_PER_PAGE) //одежда clothes
            .observeOn(AndroidSchedulers.mainThread())
    }
}