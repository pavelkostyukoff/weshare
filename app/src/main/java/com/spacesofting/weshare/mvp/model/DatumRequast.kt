package com.spacesofting.weshare.mvp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.model.Address
import com.spacesofting.weshare.mvp.model.RentPeriod


class DatumRequast {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("userId")
    var userId: String? = null

    @SerializedName("categoryId")
    var categoryId: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("address")
    var address: Address? = null

    @SerializedName("distance")
    var distance: Float? = null

    @SerializedName("images")
    var images: List<Image>? = null

    @SerializedName("rentPeriods")
    var rentPeriods: List<RentPeriod>? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

}