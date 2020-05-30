package com.spacesofting.weshare.mvp.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.context
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import com.spacesofting.weshare.mvp.presentation.presenter.IrentPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.*
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.dellAdvert

class ItemThingRentAdapter(presenter: IrentPresenter) : RecyclerView.Adapter<ItemThingRentAdapter.CompaniesListViewHolder>() {
    var dataset = ArrayList<RespounceDataMyAdverts>()
    val router = ApplicationWrapper.instance.getRouter()
    val presenter = presenter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompaniesListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view_i_rent, parent, false)
        return CompaniesListViewHolder(view)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CompaniesListViewHolder, position: Int) {
      //  holder.itemId
       // holder.carImageView = itemRent.images.get(0) as PorterShapeImageView
       // val img = itemRent.images?.get(0) as Uri
        if (dataset[position].images != null)
        {
            val output =  StringBuilder().append(dataset[position].images?.get(0)?.url?.substring(0, 4))
                .append("s").append(dataset[position].images?.get(0)?.url?.substring(4,
                    dataset[position].images?.get(0)?.url?.length!!
                )).toString()

            if (dataset[position].images!!.isNotEmpty()) {
                Picasso.with(context)
                    .load(output)
                    .placeholder(R.drawable.wish_default_img)
                    // .transform(RoundedCorners(radius))
                    .into(holder.carImageView)
            }
        }
        holder.dellAdvert.setOnClickListener {
            presenter.delAdvertById(dataset[position])
        }
        holder.editAdvert.setOnClickListener {
            presenter.getAdvert(dataset[position])
        }

       /* val innText = String.format(context.getString(R.string.edit_guest_card_inn_count), company.inn)
        holder.companyInn.text = innText

        holder.companyLayout.setOnClickListener {
            router.exitWithResult(CompaniesListFragment.SELECTED_RESULT, company)
        }*/
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