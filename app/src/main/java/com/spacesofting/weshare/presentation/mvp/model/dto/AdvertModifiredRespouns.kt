package com.spacesofting.weshare.presentation.mvp.model.dto
import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.presentation.mvp.model.Owner
import com.spacesofting.weshare.presentation.mvp.model.RentPeriod

data class AdvertModifiredRespouns(
    @SerializedName("address")
    val address: String, // Some street, 50
    @SerializedName("categoryId")
    val categoryId: String, // 00000000-0000-0000-9000-000000000000
    @SerializedName("createdAt")
    val createdAt: String, // 2020-08-09T12:33:23.588Z
    @SerializedName("description")
    val description: String, // Advert discription
    @SerializedName("id")
    val id: String, // 8ba6d01e-450d-4626-88ea-0225139fde95
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("rentPeriods")
    val rentPeriods: List<RentPeriod>,
    @SerializedName("status")
    val status: String, // published
    @SerializedName("title")
    val title: String, // Advert title
    @SerializedName("updatedAt")
    val updatedAt: String, // 2020-08-09T12:35:12.684Z
    @SerializedName("userId")
    val userId: String // 4d8006fc-8a14-49e9-935e-00bc1932eb65
)