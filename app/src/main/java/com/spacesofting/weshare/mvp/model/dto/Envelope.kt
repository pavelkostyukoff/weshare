package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Envelope {
    @SerializedName("lowerCorner")
    @Expose
    var lowerCorner: String? = null

    @SerializedName("upperCorner")
    @Expose
    var upperCorner: String? = null
}