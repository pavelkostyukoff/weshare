package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName

class Profile() {
    @SerializedName("email")
    var email: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("firstName")
    var firstName: String? = null
    @SerializedName("phone")
    var phone: String? = null
}