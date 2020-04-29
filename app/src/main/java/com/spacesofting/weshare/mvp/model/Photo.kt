package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Photo : Serializable {
    @SerializedName("id")
    val id: String? = null
    @SerializedName("sort")
    val sort: Int? = null
    @SerializedName("url")
    val url: String? = null


    /*override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || other.javaClass != this.javaClass) {
            return false
        }

        if (other is Photo) {
            if (id == other.id &&
                url == other.url ) {
                return true
            }
        }
        return false
    }
*/
/*    override fun hashCode(): Int {
        val prime = 31
        var result = pictureId
        result = prime * result + (pictureName?.hashCode() ?: 0)
        return result
    }*/
}
