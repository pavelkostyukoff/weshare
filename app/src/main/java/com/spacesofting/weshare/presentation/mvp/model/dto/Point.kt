package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Point {

    @SerializedName("pos")
    @Expose
    var pos: String? = null
}