package com.spacesofting.weshare.utils

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.api.Detail
import com.spacesofting.weshare.api.EnumErrorType

class ServerException {

    @SerializedName("type")
    var type: EnumErrorType = EnumErrorType.ERRORS

    @SerializedName("correlation_id")
    var correlationId: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("client_message")
    var clientMessage: String = ""

    @SerializedName("source")
    var source: String = ""

    @SerializedName("extra_fields")
    val extraFields: Detail? = null
}
//todo {"statusCode":400,"error":"Bad Request","message":"User already exists"}