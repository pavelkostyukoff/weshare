package com.spacesofting.weshare.presentation.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item_template.view.*

class TemplateViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    val item: View = itemView.item
    val templateImage: ImageView = itemView.templateImage
    val templateAddButton: ImageView = itemView.templateAddButton
    val templateName: TextView = itemView.templateName
    val templatePrice: TextView = itemView.templatePrice
    val profileImage: ImageView = itemView.profileImage
    val profileName: TextView = itemView.profileName
    val wallet = itemView.wallet
    val presentButton = itemView.presentButton
}