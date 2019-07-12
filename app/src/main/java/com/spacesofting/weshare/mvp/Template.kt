package com.gpbdigital.wishninja.data.dto

import com.google.gson.annotations.SerializedName
import com.gpbdigital.wishninja.ui.adapter.ListWishElement

class Template: ListWishElement {
    @SerializedName("template_id")
    override var id: Int = 0

    @SerializedName("parent_id")
    override var parentId: Int? = null

    @SerializedName("template_name")
    override var name: String? = ""

    @SerializedName("price")
    override var price: Money? = null

    @SerializedName("pictures")
    val images: Array<Image>? = null

    @SerializedName("description")
    override val description: String? = null

    @SerializedName("url")
    override var url: String? = null

    @SerializedName("status")
    override val status: ListWishElement.Status = ListWishElement.Status.UNKNOW

    @SerializedName("is_blocked")
    override var isBlocked: Boolean = false

    @SerializedName("is_complain")
    override var isComplain: Boolean = false

    @SerializedName("skip_warnings")
    var skipWarnings: Boolean = true

    @SerializedName("profile")
    override var profile: Profile? = null

    @SerializedName("additions_count")
    override var additionsCount: Int? = 0

    @Transient
    var isLoadingField = false

    @Transient
    override val type = "template"

    override fun getImage() = images?.get(0)

    override fun isLoading() = isLoadingField

    override fun setLoading(value: Boolean) {
        isLoadingField = value
    }

    override fun isClosed(): Boolean = status == ListWishElement.Status.CLOSE
}
