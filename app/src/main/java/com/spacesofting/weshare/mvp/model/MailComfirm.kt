package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName

class MailComfirm {
    @SerializedName("login")
    var login: String? = null
    @SerializedName("code")
    var code: String? = null
}