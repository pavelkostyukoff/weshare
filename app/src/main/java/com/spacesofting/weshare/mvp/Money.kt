package com.spacesofting.weshare.mvp

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class Money(): Serializable, Cloneable {

    companion object {
        val FORMATTER: DecimalFormat = NumberFormat.getCurrencyInstance(Locale.US) as DecimalFormat
        val CENT = Regex(".00\\z")
        val CENT_E= Regex(" ([0-9][0-9])\\z")
        val REPLACE = Regex("[,]")

        init {
            val symbols = DecimalFormatSymbols()

            symbols.decimalSeparator = '.'
            symbols.groupingSeparator = ' '
            symbols.currencySymbol = ""

            FORMATTER.decimalFormatSymbols = symbols
        }
    }

    constructor(amount: Number) : this() {
        this.amount = amount
    }

    @SerializedName("value")
    var amount: Number? = null

/*    @SerializedName("currency")
    var currency: Currency? = Currency.RUR

    override fun toString(): String {
        val price = FORMATTER.format(amount).replace(REPLACE, " ").replace(CENT_E, ".$1").replace(CENT, "")
        val result = "$price ${currency?.sign()}"

        return result
    }*/

    override fun equals(other: Any?): Boolean {
        return other != null
                && other is Money
                && amount?.toDouble() == other.amount?.toDouble()
              //  && currency == other.currency
    }

    override public fun clone(): Any {
        return super.clone()
    }

    fun subtract(money: Money?): Money {
        if (money == null) {
            return this
        }

        val result = Money()

   /*     if (money.amount != null && amount != null && currency == money.currency) {
            val diff = amount!!.toDouble().minus(money.amount!!.toDouble())

            result.amount = diff
            result.currency = currency
        } else {
            throw IllegalArgumentException()
        }*/

        return result
    }

    fun add(money: Money?): Money {
        if (money == null) {
            return this
        }

        val result = Money()
/*
        if (money.amount != null && amount != null && currency == money.currency) {
            val diff = amount!!.toDouble().plus(money.amount!!.toDouble())

            result.amount = diff
            result.currency = currency
        } else {
            throw IllegalArgumentException()
        }*/

        return result
    }

    override fun hashCode(): Int {
        var result = amount?.hashCode() ?: 0
       // result = 31 * result + (currency?.hashCode() ?: 0)
        return result
    }
}
