package com.gpbdigital.wishninja.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Image: Serializable, Cloneable {

    @SerializedName("picture_id")
    var id: Int? = null

    @SerializedName("picture_name")
    var name: String? = null

    @SerializedName("width")
    val width: Int? = null

    @SerializedName("height")
    val height: Int? = null

    @SerializedName("ratio")
    val aspect: Float? = null

    @SerializedName("color")
    val color: String? = null

    constructor(){}

    constructor(imgName : String?){
        name = imgName
    }

    override public fun clone(): Any {
        return super.clone()
    }

    override fun equals(other: Any?): Boolean {
        if(other != null){
            return (other as Image).name == name
        }

        return false
    }

    override fun hashCode(): Int{
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (width ?: 0)
        result = 31 * result + (height ?: 0)
        result = 31 * result + (aspect?.hashCode() ?: 0)
        result = 31 * result + (color?.hashCode() ?: 0)
        return result
    }

}
