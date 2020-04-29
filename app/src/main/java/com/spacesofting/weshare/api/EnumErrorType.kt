package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName

enum class EnumErrorType {
    @SerializedName("errors")
    ERRORS,
    @SerializedName("timeout")
    TIMEOUT,
    @SerializedName("model_validation")
    MODEL_VALIDATION,
    @SerializedName("required_confirmation")
    REQUIRED_CONFIRMATION
}