package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName

class PasswordResetComfirm {
    @SerializedName("login")
    var login: String? = null
    @SerializedName("code")
    var code: String? = null
    @SerializedName("password")
    var password: String? = null
}