package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.SerializedName

class WishList(name: String) {

    @SerializedName("wish_list_id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    init {
        this.name = name
    }
}
