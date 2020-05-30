package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.api.Image


class RespounceDataMyAdverts {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: Any? = null
    @SerializedName("description")
    var description: Any? = null
    @SerializedName("country")
    var country: Any? = null
    @SerializedName("region")
    var region: Any? = null
    @SerializedName("city")
    var city: Any? = null
    @SerializedName("address")
    var address: Any? = null
    @SerializedName("geoPoint")
    var geoPoint: Any? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("createdAt")
    var createdAt: String? = null
    @SerializedName("updatedAt")
    var updatedAt: String? = null
    @SerializedName("userId")
    var userId: String? = null
    @SerializedName("categoryId")
    var categoryId: Any? = null
    @SerializedName("images")
    var images: ArrayList<Image>? = null
    @SerializedName("rentPeriods")
    var rentPeriods: List<RentPeriod>? = null
}