package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Component {

    @SerializedName("kind")
    @Expose
    var kind: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}