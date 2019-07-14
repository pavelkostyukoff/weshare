package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("id")
    var id: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("firstName")
    var firstName: Any? = null
    @SerializedName("lastName")
    var lastName: Any? = null
    @SerializedName("birthday")
    var birthday: Any? = null
    @SerializedName("phone")
    var phone: Any? = null
    @SerializedName("avatar")
    var avatar: Any? = null
    @SerializedName("roles")
    var roles: List<Any>? = null
    @SerializedName("createdAt")
    var createdAt: Any? = null
    @SerializedName("updatedAt")
    var updatedAt: Any? = null
}