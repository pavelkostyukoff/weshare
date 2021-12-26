package com.spacesofting.weshare.presentation.mvp

import com.google.gson.annotations.SerializedName
import com.spacesofting.weshare.presentation.ui.adapter.BaseListItem
import java.io.Serializable


class Compilation(): Serializable,
    BaseListItem {

    enum class State() {
        @SerializedName("active")
        ACTIVE,
        @SerializedName("inactive")
        INACTIVE,
    }

    @SerializedName("id")
    var id: Int = 0
    @SerializedName("title")
    var title: String? = null
    @SerializedName("photo")
    var photo: Image?  = null
    @SerializedName("is_favorite")
    var isFavorite: Boolean = false
    @SerializedName("order")
    var order: Int = 0
    @SerializedName("count")
    var count: Int = 0
    @SerializedName("state")
    var state: Compilation.State? = Compilation.State.ACTIVE

    @Transient
    private var loading = false
    var isInitialized = false
    val wishList = ArrayList<Wish>()

    override fun isLoading() = loading

    override fun setLoading(value: Boolean) {
        loading = value
    }

    override fun equals(other: Any?): Boolean {
        if (other is Compilation && other.id == id && other.title == title  && other.photo == photo && other.isFavorite == isFavorite && other.order == order && count == other.count) {
            return true
        }

        return false
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        /*result = 31 * result + (listId ?: 0)*/
        result = 31 * result + (id ?: 0)
        result = 31 * result + (photo?.hashCode() ?: 0)
        result = 31 * result + (isFavorite?.hashCode() ?: 0)
        result = 31 * result + (order?.hashCode() ?: 0)
        result = 31 * result + (count?.hashCode() ?: 0)
        return result
    }
}


