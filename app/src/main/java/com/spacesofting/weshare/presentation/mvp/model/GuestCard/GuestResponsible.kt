package com.spacesofting.weshare.presentation.mvp

import com.spacesofting.weshare.presentation.mvp.model.Photo
import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.presentation.mvp.model.StructureUnitUser
import java.io.Serializable

class GuestResponsible : Serializable {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("surname")
    var surName: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                return ""
            }
            return field
        }

    @SerializedName("name")
    var firstName: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                return ""
            }
            return field
        }

    @SerializedName("additional_name")
    var lastName: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                return ""
            }
            return field
        }

    @SerializedName("photo")
    var photo: Photo? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("structure")
    var structure: StructureUnitUser? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if (other is GuestResponsible) {
            if (id == other.id &&
                    phone == other.phone &&
                    surName == other.surName &&
                    firstName == other.firstName &&
                    lastName == other.lastName &&
                    photo == other.photo &&
                    email == other.email &&
                    structure == other.structure) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = id
        result = prime * result + (phone?.hashCode() ?: 0)
        result = prime * result + (surName?.hashCode() ?: 0)
        result = prime * result + (firstName?.hashCode() ?: 0)
        result = prime * result + (lastName?.hashCode() ?: 0)
        result = prime * result + (photo?.hashCode() ?: 0)
        result = prime * result + (email?.hashCode() ?: 0)
        result = prime * result + (structure?.hashCode() ?: 0)

        return result
    }
}