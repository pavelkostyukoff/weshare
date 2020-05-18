package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName


class RentPeriod {

    @SerializedName("amount")
    var amount: Double? = null

    @SerializedName("period")
    var period: Period? = null

    @SerializedName("currency")
    var currency: Currency? = null

    enum class Period {
        HOUR,
        DAY,
        MONTH
    }

    enum class Currency {
        EURO,
        USD,
        RUB
    }
}