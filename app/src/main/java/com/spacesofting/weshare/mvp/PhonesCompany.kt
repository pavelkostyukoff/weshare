package com.spacesofting.weshare.mvp
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PhonesCompany : Serializable {
	@SerializedName("company_id")
	val companyId: Int? = null

	@SerializedName("phone_number")
	val phoneNumber: String? = null

	@SerializedName("company_phone_id")
	val companyPhoneId: Int? = null

	@SerializedName("is_default")
	val isDefault: Boolean = false
}
