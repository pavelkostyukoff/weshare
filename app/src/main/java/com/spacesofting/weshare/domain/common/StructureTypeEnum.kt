package com.spacesofting.weshare.domain.common

import com.google.gson.annotations.SerializedName
import java.io.Serializable

enum class StructureTypeEnum : Serializable {
    @SerializedName("cp")
    CP,
    @SerializedName("gssp")
    GSSP,
    @SerializedName("ssp")
    SSP,
    NONE
}