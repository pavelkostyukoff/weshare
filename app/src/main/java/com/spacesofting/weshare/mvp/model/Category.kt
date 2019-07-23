package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.BaseListItem
import java.io.Serializable
import com.google.gson.annotations.Expose


class Category {

    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null
    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null

}