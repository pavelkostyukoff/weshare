package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.model.AvatarPhoto

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
    @SerializedName("avatar")
    var avatar: AvatarPhoto? = null
    @SerializedName("birthday")
    var birthday: String? = null
}