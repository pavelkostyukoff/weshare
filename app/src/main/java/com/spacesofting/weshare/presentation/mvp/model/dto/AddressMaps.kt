package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class AddressMaps {

    @SerializedName("country_code")
    @Expose
    var countryCode: String? = null

    @SerializedName("formatted")
    @Expose
    var formatted: String? = null

    @SerializedName("postal_code")
    @Expose
    var postalCode: String? = null

    @SerializedName("Components")
    @Expose
    var components: List<Component>? = null
}