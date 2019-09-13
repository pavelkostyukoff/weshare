package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName

class NickNameStatus {
    companion object {
        val AVAILABLE       = 0
        val BLOCKED         = 1
    }

    @SerializedName("status")
    var status: Int = AVAILABLE
}
