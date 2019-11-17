package com.spacesofting.weshare.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.RentItem
import com.spacesofting.weshare.mvp.presentation.MapPresenter
import com.squareup.picasso.Picasso

class CategoryAdapter(
    val presenter: MapPresenter): RecyclerView.Adapter<TemplateViewHolder>() {
    val dataset = ArrayList<RentItem>()

    companion object {
        const val PICASSO_TAG = "picassoTag"

    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {

        val item = dataset[position]
        holder.templateName.text = item.name

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_template, parent, false)
        return TemplateViewHolder(view)
    }

    fun pauseImgDownload(context: Context){
        Picasso.with(context)
                .pauseTag(PICASSO_TAG)
    }

    fun resumeImgDownload(context: Context){
        Picasso.with(context)
                .resumeTag(PICASSO_TAG)
    }
}