package com.spacesofting.weshare.api

import com.google.gson.annotations.SerializedName

enum class EnumReasons {
    @SerializedName("operation_not_available")
    OPERATION_NOT_AVAILABLE,
    @SerializedName("not_exist")
    NOT_EXIST,
    @SerializedName("not_available")
    NOT_AVAILABLE,
    @SerializedName("not_active")
    NOT_ACTIVE,
    @SerializedName("invalid")
    INVALID,
    @SerializedName("has_been_changed")
    HAS_BEEN_CHANGED,
    @SerializedName("is_null")
    IS_NULL,
    @SerializedName("already_exists")
    ALREADY_EXISTS,
    @SerializedName("wrong_sms_code")
    WRONG_SMS_CODE,
    @SerializedName("invalid_version_token")
    INVALID_VERSION_TOKEN,
    @SerializedName("created_not_you")
    CREATED_NOT_YOU,
    @SerializedName("too_short")
    TOO_SHORT,
    @SerializedName("too_long")
    TOO_LONG,
    @SerializedName("not_uniq")
    NOT_UNIQ,
    @SerializedName("expired")
    EXPIRED,
    @SerializedName("bad_state")
    BAD_STATE
}