package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName


class Image {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("sort")
    var sort: Int? = null

    @SerializedName("url")
    var url: String? = null
}