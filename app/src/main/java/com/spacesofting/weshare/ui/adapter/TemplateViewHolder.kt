package com.spacesofting.weshare.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item_template.view.*

class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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