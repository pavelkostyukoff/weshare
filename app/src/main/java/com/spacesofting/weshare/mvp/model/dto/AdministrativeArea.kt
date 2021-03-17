package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class AdministrativeArea {
    @SerializedName("AdministrativeAreaName")
    @Expose
    var administrativeAreaName: String? = null

    @SerializedName("SubAdministrativeArea")
    @Expose
    var subAdministrativeArea: SubAdministrativeArea? = null
}