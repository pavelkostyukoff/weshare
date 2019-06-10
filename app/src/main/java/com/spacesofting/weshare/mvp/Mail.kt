package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Mail(email: String,password: String) : Serializable {

    @SerializedName("email")
    var email: String = email

    @SerializedName("password")
    var password: String = password


}
