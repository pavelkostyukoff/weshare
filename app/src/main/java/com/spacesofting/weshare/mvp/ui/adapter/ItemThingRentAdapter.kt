package com.spacesofting.weshare.mvp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.mvp.RentItem
import kotlinx.android.synthetic.main.item_card_view_i_rent.view.*

class ItemThingRentAdapter(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<ItemThingRentAdapter.CompaniesListViewHolder>() {
    val dataset = ArrayList<RentItem>()
    val router = ApplicationWrapper.instance.getRouter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompaniesListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view_i_rent, parent, false)
        return CompaniesListViewHolder(view)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CompaniesListViewHolder, position: Int) {
        val itemRent = dataset[position]
      //  holder.person_name.text = itemRent.name

    /*    val innText = String.format(context.getString(R.string.edit_guest_card_inn_count), company.inn)
        holder.companyInn.text = innText

        holder.companyLayout.setOnClickListener {
            router.exitWithResult(CompaniesListFragment.SELECTED_RESULT, company)
        }*/
    }

    class CompaniesListViewHolder(item: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(item) {
        var person_photo = item.porterShapeImageView
       // var person_name = item.person_name
      //  var person_age = item.person_age
    }
}