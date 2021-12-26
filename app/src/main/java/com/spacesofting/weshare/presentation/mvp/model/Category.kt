package com.spacesofting.weshare.presentation.mvp.model

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.presentation.mvp.Datum
import com.spacesofting.weshare.presentation.mvp.Pagination


class Category {

    @SerializedName("status")
    var status: Int? = null
    @SerializedName("data")
    var data: List<Datum>? = null
    @SerializedName("pagination")
    var pagination: Pagination? = null

}