package com.spacesofting.weshare.data.api

import com.google.gson.annotations.SerializedName
import okio.Timeout

class Detail {
    @SerializedName("token")
    var validationToken: String? = null

    @SerializedName("errors")
    var errors: ArrayList<Errors>? = null

    @SerializedName("timeout")
    var timeout: Timeout? = null
}