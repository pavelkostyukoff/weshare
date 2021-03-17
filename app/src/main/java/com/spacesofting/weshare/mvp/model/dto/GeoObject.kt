package com.spacesofting.weshare.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GeoObject {

    @SerializedName("metaDataProperty")
    @Expose
    var metaDataProperty: MetaDataProperty_? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("boundedBy")
    @Expose
    var boundedBy: BoundedBy? = null

    @SerializedName("Point")
    @Expose
    var point: Point? = null

}