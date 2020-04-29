package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName

class Errors {
	@SerializedName("field")
	val field: String? = null

	@SerializedName("reasons")
	var reasons: ArrayList<EnumReasons>? = null
}
