package com.spacesofting.weshare.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.mvp.RentItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.*

class ItemShootRentAdapter(val context: Context) : RecyclerView.Adapter<ItemShootRentAdapter.CompaniesListViewHolder>() {
    val dataset = ArrayList<RentItem>()
    val router = ApplicationWrapper.INSTANCE?.getRouter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompaniesListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view_me_shoot_rent, parent, false)
        return CompaniesListViewHolder(view)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CompaniesListViewHolder, position: Int) {
        val itemRent = dataset[position]

        Picasso.with(context).load("https://via.placeholder.com/350").into(holder.person_photo)



      //  holder.person_name.text = itemRent.name

    /*    val innText = String.format(context.getString(R.string.edit_guest_card_inn_count), company.inn)
        holder.companyInn.text = innText

        holder.companyLayout.setOnClickListener {
            router.exitWithResult(CompaniesListFragment.SELECTED_RESULT, company)
        }*/
    }

    class CompaniesListViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var person_photo = item.porterShapeImageView
       // var person_name = item.person_name
      //  var person_age = item.person_age
    }
}