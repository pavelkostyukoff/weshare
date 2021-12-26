package com.spacesofting.weshare.data.api

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.presentation.mvp.model.RentPeriod
import java.io.Serializable

class RespouncePublish : Serializable {
    @SerializedName("id")
    val id: String? = null
    @SerializedName("userId")
    val userId: String? = null
    @SerializedName("categoryId")
    val categoryId: String? = null
    @SerializedName("title")
    val title: String? = null
    @SerializedName("description")
    val description: String? = null
    @SerializedName("address")
    val address: String? = null
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