package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

enum class RoleEnum : Serializable {
    @SerializedName("ADMINISTRATOR")
    ADMINISTRATOR,

    @SerializedName("GUEST_MANAGER")
    GUEST_MANAGER,

    @SerializedName("STRUCTURE_UNIT_OFFICER")
    STRUCTURE_UNIT_OFFICER,

    @SerializedName("GUEST")
    GUEST,

    @SerializedName("EVENT_MANAGER")
    EVENT_MANAGER,

    @SerializedName("SECURITY")
    SECURITY,

    @SerializedName("GUEST_RESPONSIBLE")
    GUEST_RESPONSIBLE,

    NONE
}