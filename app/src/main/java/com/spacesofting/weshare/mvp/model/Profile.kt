package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName

class Profile( phone: String,email: String,password: String,firstName: String,lastName: String,birthday: String) {

    @SerializedName("email")
    val email: String = email
    @SerializedName("password")
    val password: String = password
    @SerializedName("firstName")
    var firstName: String = firstName
    @SerializedName("lastName")
    val lastName: String = lastName
    @SerializedName("phone")
    val phone: String = phone
    @SerializedName("birthday")
    val birthday: String = birthday

}