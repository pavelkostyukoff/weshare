package com.spacesofting.weshare.presentation.mvp.model.dto

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GeoObjectCollection {
    @SerializedName("metaDataProperty")
    @Expose
    var metaDataProperty: MetaDataProperty? = null

    @SerializedName("featureMember")
    @Expose
    var featureMember: List<FeatureMember>? = null
}