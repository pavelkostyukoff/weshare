package com.spacesofting.weshare.mvp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UpdateProfile {

    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null
    @SerializedName("lastName")
    @Expose
    var lastName: String? = null
    @SerializedName("birthday")
    @Expose
    var birthday: String? = null

}