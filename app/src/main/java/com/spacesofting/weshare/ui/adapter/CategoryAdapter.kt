package com.spacesofting.weshare.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.RentItem
import com.spacesofting.weshare.mvp.presentation.MapPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tariff.view.*
import kotlinx.android.synthetic.main.list_item_template.view.*

class CategoryAdapter(
    val presenter: MapPresenter): RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {
    val dataset = ArrayList<RentItem>()

    companion object {
        const val PICASSO_TAG = "picassoTag"

    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {

        val item = dataset[position]
        holder.carImageView.background = item.photoId

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tariff, parent, false)
        return CatViewHolder(view)
    }

    fun pauseImgDownload(context: Context){
        Picasso.with(context)
                .pauseTag(PICASSO_TAG)
    }

    fun resumeImgDownload(context: Context){
        Picasso.with(context)
                .resumeTag(PICASSO_TAG)
    }
    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carImageView: View = itemView.carImageView
      /*  val item: View = itemView.item
        val templateImage: ImageView = itemView.templateImage
        val templateAddButton: ImageView = itemView.templateAddButton
        val templateName: TextView = itemView.templateName
        val templatePrice: TextView = itemView.templatePrice
        val profileImage: ImageView = itemView.profileImage
        val profileName: TextView = itemView.profileName
        val wallet = itemView.wallet
        val presentButton = itemView.presentButton*/
    }

}