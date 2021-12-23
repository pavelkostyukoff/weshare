package com.spacesofting.weshare.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts


class ProductDiffUtilCallback(
    private val oldList: List<RespounceDataMyAdverts>,
    private val newList: List<RespounceDataMyAdverts>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.id === newProduct.id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return (oldProduct.description?.equals(newProduct.description)!!
                && oldProduct.address === newProduct.address)
    }

}