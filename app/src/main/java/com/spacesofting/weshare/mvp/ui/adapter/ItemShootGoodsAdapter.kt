package com.spacesofting.weshare.mvp.ui.adapter

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pawegio.kandroid.displayWidth
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.context
import com.spacesofting.weshare.common.BoomerangoRouter
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import com.spacesofting.weshare.mvp.presentation.presenter.IrentPresenter
import com.spacesofting.weshare.utils.dp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.*


/*
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.dellAdvert
*/

class ItemShootGoodsAdapter(val presenter: IrentPresenter) : RecyclerView.Adapter<ItemShootGoodsAdapter.CompaniesListViewHolder>() {
    var dataset = ArrayList<RespounceDataMyAdverts>()
    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
    val router: BoomerangoRouter = ApplicationWrapper.instance.getRouter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompaniesListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_card_view_i_rent,
            parent,
            false
        )
        view.carImageView.layoutParams.apply {
            this.height = getScreenHeight()
            this.width = getScreenWidth()
        }
        return CompaniesListViewHolder(view)
    }
    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: CompaniesListViewHolder, position: Int) {
       val  positionOne = dataset[position]
        if (positionOne.images != null )
        {
                val output =  StringBuilder().append(
                    positionOne.images?.get(0)?.url?.substring(
                        0,
                        4
                    )
                )
                    .append("s").append(positionOne.images?.get(0)?.url?.length?.let {
                        positionOne.images?.get(0)?.url?.substring(4, it)
                    }).toString()
                if (output.isNotEmpty()) {
                    val width = context.displayWidth - 16.dp * 2
                    val height = context.resources.getDimensionPixelOffset(R.dimen.image_width)

                    Picasso.with(context)
                        .load("output")//http://res.cloudinary.com/covenant61/image/upload/v1620077794/boomerango/adverts/2e702ba8-92e3-4587-80dc-72d902bcee5d/13a81da4-f796-4829-8f56-b2eec5f5ba30.jpg
                       // .resize(width,height)
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
        val carImageView: ImageView = itemView.carImageView
        val dellAdvert: ImageView = itemView.dellAdvert
        val editAdvert: ImageView = itemView.editAdvert

        // var person_name = item.person_name
      //  var person_age = item.person_age
    }
}