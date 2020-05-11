package com.spacesofting.weshare.mvp.model
import com.google.gson.annotations.SerializedName


class Advert {

    @SerializedName("title")
    var title: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("rentPeriods")
    var rentPeriods: List<RentPeriod>? = null
    @SerializedName("categoryId")
    var categoryId: String? = null
    @SerializedName("address")
    var address: Address? = null

}