package com.spacesofting.weshare.data.api

import com.google.gson.annotations.SerializedName


class Entitys {
	@SerializedName("entities")
	var entities: ArrayList<Entity>? = null

	@SerializedName("limit")
	val limit: Int? = null

	@SerializedName("offset")
	val offset: Int? = null

	@SerializedName("total")
	val total: Int? = null
}
