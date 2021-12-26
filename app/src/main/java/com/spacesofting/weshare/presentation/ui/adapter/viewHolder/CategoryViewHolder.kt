package com.spacesofting.weshare.presentation.ui.adapter.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.ui.adapter.Category
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    constructor(parent: ViewGroup) :
            this(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))

    fun bind(category: Category) {
        itemView.categoryName.text = category.name
    }

}