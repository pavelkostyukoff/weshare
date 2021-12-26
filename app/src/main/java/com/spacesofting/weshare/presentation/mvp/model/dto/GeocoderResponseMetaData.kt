package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GeocoderResponseMetaData {
    @SerializedName("request")
    @Expose
    var request: String? = null

    @SerializedName("results")
    @Expose
    var results: String? = null

    @SerializedName("found")
    @Expose
    var found: String? = null
}