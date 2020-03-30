package com.spacesofting.weshare.mvp.ui.adapter

interface BaseListItem {
    /**
     * Base item interface
     */

    fun isLoading() : Boolean
    fun setLoading(value: Boolean)
}
