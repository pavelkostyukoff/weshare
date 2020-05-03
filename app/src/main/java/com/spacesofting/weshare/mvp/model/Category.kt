package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.Datum
import com.spacesofting.weshare.mvp.Pagination


class Category {

    @SerializedName("status")
    var status: Int? = null
    @SerializedName("data")
    var data: List<Datum>? = null
    @SerializedName("pagination")
    var pagination: Pagination? = null

}