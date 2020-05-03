package com.spacesofting.weshare.mvp.model
import com.google.gson.annotations.SerializedName


class Point {
    @SerializedName("latitude")
    var latitude: String? = null
    @SerializedName("longitude")
    var longitude: String? = null
}