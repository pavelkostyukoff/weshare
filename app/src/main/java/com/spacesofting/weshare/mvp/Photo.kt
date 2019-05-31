package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Photo : Serializable {
    @SerializedName("picture_id")
    val pictureId: Int = 0

    @SerializedName("picture_name")
    val pictureName: String? = null

    @SerializedName("width")
    val width: Int = 0

    @SerializedName("height")
    val height: Int = 0

    @SerializedName("ratio")
    val ratio: Double? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if (other is Photo) {
            if (pictureId == other.pictureId &&
                    pictureName == other.pictureName &&
                    width == other.width &&
                    height == other.height &&
                    ratio == other.ratio) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = pictureId
        result = prime * result + (pictureName?.hashCode() ?: 0)
        result = prime * result + width
        result = prime * result + height
        result = prime * result + (ratio?.hashCode() ?: 0)
        return result
    }
}
