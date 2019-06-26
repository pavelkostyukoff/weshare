package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import com.google.gson.annotations.Expose



class Photo : Serializable {
    @SerializedName("id")
    val pictureId: Int = 0

    @SerializedName("url")
    val pictureName: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if (other is Photo) {
            if (pictureId == other.pictureId &&
                    pictureName == other.pictureName ) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = pictureId
        result = prime * result + (pictureName?.hashCode() ?: 0)
        return result
    }
}
