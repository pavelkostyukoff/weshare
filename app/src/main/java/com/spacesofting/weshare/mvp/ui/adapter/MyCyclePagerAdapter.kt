package com.spacesofting.weshare.mvp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.context
import com.squareup.picasso.Picasso
import com.wangpeiyuan.cycleviewpager2.adapter.CyclePagerAdapter
import kotlinx.android.synthetic.main.fragment_pager.*
import java.io.File


class MyCyclePagerAdapter :  CyclePagerAdapter<MyCyclePagerAdapter.PagerViewHolder>() {
    var dataset = ArrayList<File>()

    // создаем поле объекта-колбэка
    private var mListener: OnCardClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
       // Log.d(AddGoodsFragment.TAG, "onCreateViewHolder")
        return PagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_pager, parent, false)
        )
    }

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wishEditImageBtn: ImageButton = itemView.findViewById(R.id.wishEditImageBtn)
        var wishEditImageView: com.makeramen.roundedimageview.RoundedImageView = itemView.findViewById(R.id.wishEditImageView)
        var dellImage: ImageView = itemView.findViewById(R.id.dellImage)
    }


    override fun getRealItemCount(): Int = dataset.size


    override fun onBindRealViewHolder(holder: PagerViewHolder, position: Int) {
/*
        val item = dataset[position]
        holder.carImageView.background = item.photoId*/
       // Log.d(AddGoodsFragment.TAG, "onBindRealViewHolder $position")

        Picasso.with(context)
            .load(dataset[position])
           // .transform(RoundedCorners(radius))
            .into(holder.wishEditImageView)

        holder.wishEditImageBtn.setOnClickListener {
            mListener?.onCardClick()
        }
        holder.dellImage.setOnClickListener {
            //todo презентер удаляет фото и говоит отобразить пикассо заглушку
              mListener?.onCardClickDelete()
          //  privacyOptions
            //после успешного удаления скрываем   dellImage.visibility = View.GONE
        }
        if (dataset.isNotEmpty()) {
            val test = dataset[0].name
            if (test == ""){
                holder.dellImage.visibility = View.GONE
            }
            else {
                holder.dellImage.visibility = View.VISIBLE
            }
        }
        else {
            holder.dellImage.visibility = View.VISIBLE
        }
    }
    // создаем сам интерфейс и указываем метод и передаваемые им аргументы
// View на котором произошло событие и позиция этого View
    interface OnCardClickListener {
        fun onCardClick(/*view: View?, position: Int*/)
        fun onCardClickDelete(/*view: View?, position: Int*/)
    }


    // метод-сеттер для привязки колбэка к получателю событий
    fun setOnCardClickListener(listener: OnCardClickListener) {
        mListener = listener
    }
}