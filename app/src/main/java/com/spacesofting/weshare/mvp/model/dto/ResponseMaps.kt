package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ResponseMaps {

    @SerializedName("response")
    @Expose
    var response: Response? = null
}