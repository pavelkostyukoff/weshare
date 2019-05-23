package com.digitalhorizon.eve.mvp.model.guestcard

import com.digitalhorizon.eve.mvp.model.UpdateCompany
import com.digitalhorizon.eve.mvp.model.UpdateGuestPriority
import com.digitalhorizon.eve.mvp.model.UpdateId
import com.digitalhorizon.eve.mvp.model.UpdatePicture
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GuestCardRequest : Serializable {
    @SerializedName("company")
    var company: UpdateCompany = UpdateCompany()

    @SerializedName("position")
    var position: String = ""

    @SerializedName("priority")
    var priority: UpdateGuestPriority = UpdateGuestPriority()

    @SerializedName("comment")
    var comment: String? = null

    @SerializedName("accompanyings")
    var accompanyings: String? = null

    @SerializedName("structure_unit")
    var structureUnit: UpdateId = UpdateId()

    @SerializedName("guest_responsible")
    var guestResponsible: UpdateId = UpdateId()

    @SerializedName("phone")
    var phone: String = ""

    @SerializedName("surname")
    var surName: String = ""

    @SerializedName("name")
    var firstName: String = ""

    @SerializedName("additional_name")
    var lastName: String? = null

    @SerializedName("photo")
    var photo: UpdatePicture? = UpdatePicture()

    @SerializedName("email")
    var email: String = ""
}