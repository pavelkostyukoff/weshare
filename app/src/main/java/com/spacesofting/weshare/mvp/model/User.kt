package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class User {

    @SerializedName("id")
    val id: String? = null
    @SerializedName("email")
    val email: String? = null
    @SerializedName("firstName")
    val firstName: String? = null
    @SerializedName("lastName")
    var lastName: String? = null
    @SerializedName("birthday")
    var birthday: String? = null
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("avatar")
    val avatar: String? = null
    @SerializedName("roles")
    val roles: List<String>? = null
    @SerializedName("createdAt")
    val createdAt: Any? = null
    @SerializedName("updatedAt")
    val updatedAt: String? = null
}