package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StructureUnitRoles : Serializable {
    @SerializedName("structure_unit_id")
    var structureUnitId: Int = 0

    @SerializedName("structure_unit_name")
    var structureUnitName: String = ""

    @SerializedName("role")
    var role: String = ""
}