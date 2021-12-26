package com.spacesofting.weshare.presentation.mvp.model
import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("avatar")
    val avatar: String, // http://res.cloudinary.com/covenant61/image/upload/v1616328517/boomerango/avatars/4d8006fc-8a14-49e9-935e-00bc1932eb65/ea5f0671-b233-41ae-ae31-d9e01ea35a78.jpg
    @SerializedName("firstName")
    val firstName: String, // John
    @SerializedName("id")
    val id: String, // 4d8006fc-8a14-49e9-935e-00bc1932eb65
    @SerializedName("lastName")
    val lastName: String, // Smith
    @SerializedName("phone")
    val phone: String // +79000000000
)