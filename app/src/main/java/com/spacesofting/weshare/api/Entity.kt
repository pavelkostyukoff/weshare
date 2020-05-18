package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName


class Entity {
	@SerializedName("id")
	val id: String? = null
	@SerializedName("name")
    var name: String? = null
	@SerializedName("code")
	val code: String? = null
	@SerializedName("parentId")
	var parentId: String? = null
	var categoryImg: String? = null
}
