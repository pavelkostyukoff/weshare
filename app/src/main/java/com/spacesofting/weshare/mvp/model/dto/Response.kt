package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Response {

    @SerializedName("GeoObjectCollection")
    @Expose
    var geoObjectCollection: GeoObjectCollection? = null
}