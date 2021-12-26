package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName

class ResponceProfile {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("firstName")
    val firstName : String? = null
    @SerializedName("lastName")
    val lastName : String? = null
    @SerializedName("createdAt")
    val createdAt : String? = null
    @SerializedName("birthday")
    val birthday : String? = null

}
