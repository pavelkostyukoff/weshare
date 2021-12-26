package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Login() : Serializable {

    @SerializedName("login")
    var login: String? = null

    @SerializedName("password")
    var password: String? = null

}
