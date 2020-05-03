package com.spacesofting.weshare.mvp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.RentItem
import com.spacesofting.weshare.mvp.presentation.presenter.MapPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tariff.view.*

class CategoryAdapter(
    val presenter: MapPresenter
): RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {
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
    class CatViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
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