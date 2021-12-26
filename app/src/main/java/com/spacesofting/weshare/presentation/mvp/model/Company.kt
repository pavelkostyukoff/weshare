package com.spacesofting.weshare.presentation.mvp.model
import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.presentation.mvp.PhonesCompany
import java.io.Serializable
import java.util.ArrayList

class Company : Serializable {
    @SerializedName("company_id")
    var companyId: Int = 0

    @SerializedName("company_name")
    var companyName: String? = null

    @SerializedName("inn")
    var inn: String? = null

    @SerializedName("phones")
    var phones: List<PhonesCompany>? = ArrayList()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if(other is Company) {
            if (companyId == other.companyId && companyName == other.companyName && inn == other.inn) {
                if (phones != null && other.phones != null && phones!!.containsAll(other.phones!!)) {
                    return true
                }
                if (phones == null && other.phones == null) {
                    return true
                }
            }
        }

        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = companyId
        result = prime * result + (companyName?.hashCode() ?: 0)
        result = prime * result + (inn?.hashCode() ?: 0)
        result = prime * result + (phones?.hashCode() ?: 0)
        return result
    }
}