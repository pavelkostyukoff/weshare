package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.mvp.model.RentPeriod
import java.io.Serializable

class ResponcePublish : Serializable {
    @SerializedName("id")
    val id: String? = null
    @SerializedName("userId")
    val userId: String? = null
    @SerializedName("categoryId")
    val categoryId: Any? = null
    @SerializedName("title")
    val title: Any? = null
    @SerializedName("description")
    val description: Any? = null
    @SerializedName("address")
    val address: Any? = null
    @SerializedName("images")
    val images: List<Image>? = null
    @SerializedName("rentPeriods")
    val rentPeriods: List<RentPeriod>? = null
    @SerializedName("status")
    val status: String? = null
    @SerializedName("createdAt")
    val createdAt: String? = null
    @SerializedName("updatedAt")
    val updatedAt: String? = null
}