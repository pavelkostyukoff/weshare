package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PostalCode {
    @SerializedName("PostalCodeNumber")
    @Expose
    var postalCodeNumber: String? = null
}