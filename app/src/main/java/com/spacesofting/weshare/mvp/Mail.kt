package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Mail(token: String) : Serializable {

    @SerializedName("token")
    var token: String = token

}
