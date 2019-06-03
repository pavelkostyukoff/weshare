package com.spacesofting.weshare.mvp.device

import android.os.Build
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OS : Serializable {
    @SerializedName("type")
    val type = "android"

    @SerializedName("name")
    val name: String = Build.VERSION_CODES::class.java.fields[android.os.Build.VERSION.SDK_INT].name

    @SerializedName("version")
    val version: Int = android.os.Build.VERSION.SDK_INT
}