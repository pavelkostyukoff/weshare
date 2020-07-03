package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Refrash(tocken: String) : Serializable {

    @SerializedName("token")
    var refreshToken: String = tocken

}
