package com.spacesofting.weshare.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.Datum
import com.spacesofting.weshare.mvp.DatumRequast
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import java.io.Serializable


class ResponceMyAdvertMaps : Serializable {
    @SerializedName("data")
    var data: ArrayList<DatumRequast>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("limit")
    var limit: Int? = null

    @SerializedName("offset")
    var offset: Int? = null
}