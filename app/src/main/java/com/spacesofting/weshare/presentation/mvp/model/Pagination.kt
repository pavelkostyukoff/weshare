package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName
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