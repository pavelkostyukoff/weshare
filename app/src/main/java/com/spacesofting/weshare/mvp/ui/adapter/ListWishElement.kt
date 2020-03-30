package com.spacesofting.weshare.mvp.ui.adapter

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.Image
import com.spacesofting.weshare.mvp.Profile
import java.io.Serializable

interface ListWishElement : Serializable ,
    BaseListItem {

    enum class Status {
        @SerializedName("active")
        ACTIVE,
        @SerializedName("close")
        CLOSE,
        @SerializedName("temporary")
        TEMPORARY,
        @SerializedName("unknow")
        UNKNOW;
    }

    var id: Int
    var parentId: Int?
    var name: String?
//    var price: Money?
    var url: String?
    var isBlocked: Boolean
    var isComplain: Boolean
    var additionsCount: Int?
    val type: String
    val description: String?
    val profile: Profile?
    val status: Status

    fun getImage(): Image?
    fun isClosed(): Boolean
}