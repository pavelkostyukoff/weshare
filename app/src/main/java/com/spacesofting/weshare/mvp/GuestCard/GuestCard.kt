package com.spacesofting.weshare.mvp

import com.spacesofting.weshare.mvp.model.Company
import com.spacesofting.weshare.mvp.model.Photo
import com.spacesofting.weshare.mvp.model.guestcard.GuestCardPriority
import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.common.StructureUnit
import com.spacesofting.weshare.mvp.model.guestcard.GuestCardState
import java.io.Serializable

class GuestCard : Serializable {
    @SerializedName("company_id")
    var companyId: Int = 0

    @SerializedName("photo")
    var photo: Photo? = null

    @SerializedName("priority")
    var priority: GuestCardPriority = GuestCardPriority()

    @SerializedName("guest_card_id")
    var id: Int = 0

    @SerializedName("row_version")
    var rowVersion: String? = null

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("surname")
    var surname: String? = null
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

    @SerializedName("name")
    var firstName: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                return ""
            }
            return field
        }

    @SerializedName("accompanyings")
    var accompanyings: String? = null

    @SerializedName("comment")
    var comment: String? = null

    @SerializedName("company")
    var company: Company? = null

    @SerializedName("position")
    var position: String? = null

    @SerializedName("state")
    var state: GuestCardState = GuestCardState.AGREED

    @SerializedName("email")
    var email: String? = null

    @SerializedName("structure_unit_id")
    var structureUnitId: Int = 0

    @SerializedName("structure_unit")
    var structureUnit: StructureUnit = StructureUnit()

    @SerializedName("guest_card_operations")
    var guestCardOperations: GuestCardOperations = GuestCardOperations()

    @SerializedName("guest_responsible")
    var guestResponsible: GuestResponsible = GuestResponsible()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if (other is GuestCard) {
            if (companyId == other.companyId &&
                    photo == other.photo &&
                    priority == other.priority &&
                    id == other.id &&
                    rowVersion == other.rowVersion &&
                    phone == other.phone &&
                    surname == other.surname &&
                    lastName == other.lastName &&
                    firstName == other.firstName &&
                    accompanyings == other.accompanyings &&
                    comment == other.comment &&
                    company == other.company &&
                    position == other.position &&
                    state == other.state &&
                    email == other.email &&
                    structureUnitId == other.structureUnitId &&
                    structureUnit == other.structureUnit &&
                    guestCardOperations == other.guestCardOperations &&
                    guestResponsible == other.guestResponsible) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = companyId
        result = prime * result + (photo?.hashCode() ?: 0)
        result = prime * result + priority.hashCode()
        result = prime * result + id
        result = prime * result + (rowVersion?.hashCode() ?: 0)
        result = prime * result + (phone?.hashCode() ?: 0)
        result = prime * result + (surname?.hashCode() ?: 0)
        result = prime * result + (lastName?.hashCode() ?: 0)
        result = prime * result + (firstName?.hashCode() ?: 0)
        result = prime * result + (accompanyings?.hashCode() ?: 0)
        result = prime * result + (comment?.hashCode() ?: 0)
        result = prime * result + (company?.hashCode() ?: 0)
        result = prime * result + (position?.hashCode() ?: 0)
        result = prime * result + state.hashCode()
        result = prime * result + (email?.hashCode() ?: 0)
        result = prime * result + structureUnitId
        result = prime * result + structureUnit.hashCode()
        result = prime * result + guestCardOperations.hashCode()
        result = prime * result + guestResponsible.hashCode()

        return result
    }
}
