package com.spacesofting.weshare.mvp.model.dto
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    val id: String, // 1e3ebfaf-ea21-4235-91dd-02a64de602c2
    @SerializedName("sort")
    val sort: Int, // 1000
    @SerializedName("url")
    val url: String // http://res.cloudinary.com/dou7ospie/image/upload/v1596976425/boomerango/adverts/8ba6d01e-450d-4626-88ea-0225139fde95/1e3ebfaf-ea21-4235-91dd-02a64de602c2.png
)