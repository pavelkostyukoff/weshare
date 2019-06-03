package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Registration(token: String) : Serializable {

    @SerializedName("token")
    var token: String = token

    @SerializedName("refreshToken")
    var rowrefreshTokenVersion: String? = null

    @SerializedName("user")
    var user: User? = null


}

class User {

}
