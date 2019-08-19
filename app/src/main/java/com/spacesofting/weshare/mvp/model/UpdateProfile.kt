package com.spacesofting.weshare.mvp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UpdateProfile {

    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("firstName")
    var firstName: String? = null
    @SerializedName("lastName")
    var lastName: String? = null
    @SerializedName("birthday")
    var birthday: String? = null

}