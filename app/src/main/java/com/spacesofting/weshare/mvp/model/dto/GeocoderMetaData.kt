package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GeocoderMetaData {
    @SerializedName("precision")
    @Expose
    var precision: String? = null

    @SerializedName("text")
    @Expose
    var text: String? = null

    @SerializedName("kind")
    @Expose
    var kind: String? = null

    @SerializedName("Address")
    @Expose
    var address: AddressMaps? = null

    @SerializedName("AddressDetails")
    @Expose
    var addressDetails: AddressDetails? = null
}