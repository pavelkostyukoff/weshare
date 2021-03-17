package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class SubAdministrativeArea {
    @SerializedName("SubAdministrativeAreaName")
    @Expose
    var subAdministrativeAreaName: String? = null

    @SerializedName("Locality")
    @Expose
    var locality: Locality? = null
}