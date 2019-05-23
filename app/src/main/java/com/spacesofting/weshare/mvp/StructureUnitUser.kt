package com.digitalhorizon.eve.mvp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StructureUnitUser : Serializable {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("role")
    var role: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if (other is StructureUnitUser) {
            if (id == other.id &&
                    role == other.role) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = id
        result = prime * result + role.hashCode()
        return result
    }
}