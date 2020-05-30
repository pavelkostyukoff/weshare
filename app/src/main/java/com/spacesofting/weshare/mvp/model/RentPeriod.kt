package com.spacesofting.weshare.mvp.model

import com.google.gson.annotations.SerializedName


class RentPeriod {

    @SerializedName("amount")
    var amount: Double? = null

    @SerializedName("period")
    var period: Period? = null

    @SerializedName("currency")
    var currency: Currency? = null
//hour,day,week,month
    enum class Period {
        hour,
        day,
/*
        week,
*/
        month
    }
//RUB,USD,EUR
    enum class Currency {
        EUR,
        USD,
        RUB
    }
}