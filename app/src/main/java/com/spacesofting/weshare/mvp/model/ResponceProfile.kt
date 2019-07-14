package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName

class ResponceProfile {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("email")
    val email : String? = null
    @SerializedName("firstName")
    val firstName : String? = null
    @SerializedName("lastName")
    val lastName : String? = null
    @SerializedName("birthday")
    val birthday : String? = null
    @SerializedName("phone")
    val phone : String? = null
    @SerializedName("avatar")
    val avatar : String? = null
    @SerializedName("roles")
    val roles: List<String>? = null

    @SerializedName("createdAt")
    val createdAt : String? = null
    @SerializedName("updatedAt")
    val updatedAt : String? = null

}
