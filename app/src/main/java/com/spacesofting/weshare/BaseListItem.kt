package com.gpbdigital.wishninja.ui.adapter

interface BaseListItem {
    /**
     * Base item interface
     */

    fun isLoading() : Boolean
    fun setLoading(value: Boolean)
}
