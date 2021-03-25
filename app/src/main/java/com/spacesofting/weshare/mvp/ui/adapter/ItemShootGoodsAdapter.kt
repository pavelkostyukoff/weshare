package com.spacesofting.weshare.mvp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.context
import com.spacesofting.weshare.common.BoomerangoRouter
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import com.spacesofting.weshare.mvp.presentation.presenter.IrentPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.*
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.dellAdvert

class ItemShootGoodsAdapter(val presenter: IrentPresenter) : RecyclerView.Adapter<ItemShootGoodsAdapter.CompaniesListViewHolder>() {
    var dataset = ArrayList<RespounceDataMyAdverts>()
    val router: BoomerangoRouter = ApplicationWrapper.instance.getRouter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompaniesListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view_i_rent, parent, false)
        return CompaniesListViewHolder(view)
    }
    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: CompaniesListViewHolder, position: Int) {
       val  positionOne = dataset[position]
        if (positionOne.images != null )
        {
                val output =  StringBuilder().append(positionOne.images?.get(0)?.url?.substring(0, 4))
                    .append("s").append(positionOne.images?.get(0)?.url?.length?.let {
                        positionOne.images?.get(0)?.url?.substring(4, it)
                    }).toString()
                if (positionOne.images!!.isNotEmpty()) {
                    Picasso.with(context)
                        .load(output)
                        .placeholder(R.drawable.wish_default_img)
                        // .transform(RoundedCorners(radius))
                        .into(holder.carImageView)
                }
            }
        holder.dellAdvert.setOnClickListener {
            presenter.delAdvertById(positionOne)
            dataset.remove(positionOne)
            this.notifyDataSetChanged()
        }
        holder.editAdvert.setOnClickListener {
            presenter.editAdverts(positionOne)
        }
    }

    class CompaniesListViewHolder(item: View) : RecyclerView.ViewHolder(item) {
      //  var person_photo = item.porterShapeImageView
        val carImageView: PorterShapeImageView = itemView.porterShapeImageView
        val dellAdvert: ImageView = itemView.dellAdvert
        val editAdvert: ImageView = itemView.editAdvert

        // var person_name = item.person_name
      //  var person_age = item.person_age
    }
}