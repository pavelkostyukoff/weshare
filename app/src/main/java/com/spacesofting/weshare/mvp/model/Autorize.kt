package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Autorize(token: String) : Serializable {

    @SerializedName("accessToken")
    var accessToken: String = token

    @SerializedName("refreshToken")
    var rowrefreshTokenVersion: String? = null
/*
    @SerializedName("user")
    var user: User? = null*/
}