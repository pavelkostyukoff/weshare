package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Autorize(token: String) : Serializable {

    @SerializedName("accessToken")
    var accessToken: String = token

    @SerializedName("refreshToken")
    var refreshToken: String? = null
}