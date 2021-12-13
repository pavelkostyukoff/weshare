package com.spacesofting.weshare.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spacesofting.weshare.ui.adapter.viewHolder.CategoryViewHolder

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {
    var list: List<Category> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(list[position])
        val item = position

    }

    fun getItem(position: Int): Category {
        return list[position]
    }


    fun setItems(list: List<Category>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}
