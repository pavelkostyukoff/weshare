package com.spacesofting.weshare

interface BaseListItem {
    /**
     * Base item interface
     */

    fun isLoading() : Boolean
    fun setLoading(value: Boolean)
}
