package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MetaDataProperty {
    @SerializedName("GeocoderResponseMetaData")
    @Expose
    var geocoderResponseMetaData: GeocoderResponseMetaData? = null
}