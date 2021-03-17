package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Locality {
    @SerializedName("LocalityName")
    @Expose
    var localityName: String? = null

    @SerializedName("Thoroughfare")
    @Expose
    var thoroughfare: Thoroughfare? = null
}