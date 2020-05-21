package com.spacesofting.weshare.mvp.model
import com.google.gson.annotations.SerializedName


class Address {

    @SerializedName("country")
    var country: String? = "Russia"
    @SerializedName("region")
    var region: String? = null
    @SerializedName("city")
    var city: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("point")
    var point: Point? = null

}