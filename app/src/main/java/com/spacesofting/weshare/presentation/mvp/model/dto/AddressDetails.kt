package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class AddressDetails {
    @SerializedName("Country")
    @Expose
    var country: Country? = null
}