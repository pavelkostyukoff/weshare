package com.spacesofting.weshare.mvp.device

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.device.OS
import java.io.Serializable

class DeviceInfo : Serializable {
    @SerializedName("id")
    var id: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("version")
    var model: String = ""

    @SerializedName("vendor")
    var vendor: String = ""

    @SerializedName("imei")
    var imei: String = ""

    @SerializedName("os")
    var os: OS = OS()

    @SerializedName("macs")
    var macs: ArrayList<String> = ArrayList()

    @SerializedName("push_token")
    var pushToken: String = ""
}