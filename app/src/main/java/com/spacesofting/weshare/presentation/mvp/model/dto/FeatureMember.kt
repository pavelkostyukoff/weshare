package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FeatureMember {
    @SerializedName("GeoObject")
    @Expose
    var geoObject: GeoObject? = null
}