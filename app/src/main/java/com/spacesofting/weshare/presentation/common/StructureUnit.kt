package com.spacesofting.weshare.presentation.common

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StructureUnit : Serializable {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("type")
    var type: StructureTypeEnum = StructureTypeEnum.NONE

    @SerializedName("name")
    var name: String = ""

    @SerializedName("parent_id")
    var parentId: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if (other is StructureUnit) {
            if (id == other.id &&
                    type == other.type &&
                    name == other.name &&
                    parentId == other.parentId) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = id
        result = prime * result + type.hashCode()
        result = prime * result + name.hashCode()
        result = prime * result + parentId

        return result
    }
}