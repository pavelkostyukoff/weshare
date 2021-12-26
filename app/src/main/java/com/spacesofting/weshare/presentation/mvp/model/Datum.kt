package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


class Datum {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("alias")
    @Expose
    var alias: String? = null
    @SerializedName("tags")
    @Expose
    var tags: List<String>? = null

}