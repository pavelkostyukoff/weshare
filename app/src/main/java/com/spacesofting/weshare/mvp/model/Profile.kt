package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName

class Profile {
    @SerializedName("login")
    var login: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("firstName")
    var firstName: String? = null
    @SerializedName("lastName")
    var lastName: String? = null
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("birthday")
    var birthday: String? = null
}