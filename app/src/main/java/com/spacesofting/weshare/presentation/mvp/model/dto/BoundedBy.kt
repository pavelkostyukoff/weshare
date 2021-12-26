package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class BoundedBy {

    @SerializedName("Envelope")
    @Expose
    var envelope: Envelope? = null
}