package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class AddressDetails {
    @SerializedName("Country")
    @Expose
    var country: Country? = null
}