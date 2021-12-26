package com.spacesofting.weshare.presentation.ui.adapter

interface BaseListItem {
    /**
     * Base item interface
     */

    fun isLoading() : Boolean
    fun setLoading(value: Boolean)
}
