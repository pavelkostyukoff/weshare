package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.BaseListItem
import java.io.Serializable
import com.google.gson.annotations.Expose

class Pagination {

    @SerializedName("total")
    @Expose
    var total: Int? = null
    @SerializedName("limit")
    @Expose
    var limit: Int? = null
    @SerializedName("offset")
    @Expose
    var offset: Int? = null

}