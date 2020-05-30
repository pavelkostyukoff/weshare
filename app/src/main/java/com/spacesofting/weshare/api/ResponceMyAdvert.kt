package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import java.io.Serializable


class ResponceMyAdvert : Serializable {
    @SerializedName("data")
    var data: ArrayList<RespounceDataMyAdverts>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("limit")
    var limit: Int? = null

    @SerializedName("offset")
    var offset: Int? = null
}