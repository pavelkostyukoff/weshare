package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName


class Image {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("advertId")
    var advertId: String? = null

    @SerializedName("sort")
    var sort: Int? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

}