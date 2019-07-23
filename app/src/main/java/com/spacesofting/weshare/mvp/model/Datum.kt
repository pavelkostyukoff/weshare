package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.BaseListItem
import java.io.Serializable
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