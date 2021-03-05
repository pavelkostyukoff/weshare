package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Refresh(tocken: String) : Serializable {
    @SerializedName("token")
    var refreshToken: String = tocken
}