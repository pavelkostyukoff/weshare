package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Premise {

    @SerializedName("PremiseNumber")
    @Expose
    var premiseNumber: String? = null

    @SerializedName("PostalCode")
    @Expose
    var postalCode: PostalCode? = null
}