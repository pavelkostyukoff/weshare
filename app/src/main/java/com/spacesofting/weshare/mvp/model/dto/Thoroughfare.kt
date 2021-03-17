package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Thoroughfare {
    @SerializedName("ThoroughfareName")
    @Expose
    var thoroughfareName: String? = null

    @SerializedName("Premise")
    @Expose
    var premise: Premise? = null
}