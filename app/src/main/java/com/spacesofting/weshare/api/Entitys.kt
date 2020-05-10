package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName


class Entitys {
	@SerializedName("entities")
	val entities: ArrayList<Entity>? = null

	@SerializedName("limit")
	val limit: Int? = null

	@SerializedName("offset")
	val offset: Int? = null

	@SerializedName("total")
	val total: Int? = null
}