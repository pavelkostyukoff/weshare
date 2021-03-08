package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Image: Serializable, Cloneable {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("sort")
    val sort: Int? = null

    override fun equals(other: Any?): Boolean {
        if(other != null){
            return (other as Image).url == url
        }
        return false
    }

    override fun hashCode(): Int{
        var result = sort ?: 0
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (sort ?: 0)
        return result
    }

}
