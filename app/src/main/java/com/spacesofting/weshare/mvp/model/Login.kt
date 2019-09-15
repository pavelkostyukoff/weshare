package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Login() : Serializable {

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

}
