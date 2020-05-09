package com.spacesofting.weshare.mvp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.ui.fragment.AddGoodsFragment
import com.wangpeiyuan.cycleviewpager2.adapter.CyclePagerAdapter


class MyCyclePagerAdapter :  CyclePagerAdapter<MyCyclePagerAdapter.PagerViewHolder>() {
    var dataset = ArrayList<Int>()
    private val imageArr = intArrayOf(
        android.R.drawable.ic_dialog_info,
        android.R.drawable.ic_delete,
        android.R.drawable.ic_delete,
        android.R.drawable.ic_delete)

    // создаем поле объекта-колбэка
    private var mListener: OnCardClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        Log.d(AddGoodsFragment.TAG, "onCreateViewHolder")
        return PagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_pager, parent, false)
        )
    }

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wishEditImageBtn: ImageButton = itemView.findViewById(R.id.wishEditImageBtn)
        var dellImage: ImageView = itemView.findViewById(R.id.dellImage)
    }


    override fun getRealItemCount(): Int = dataset.size


    override fun onBindRealViewHolder(holder: PagerViewHolder, position: Int) {
/*
        val item = dataset[position]
        holder.carImageView.background = item.photoId*/
        Log.d(AddGoodsFragment.TAG, "onBindRealViewHolder $position")
       /* Picasso.with(context)
            .load(dataset[position])
            .into(holder.wishEditImageBtn)*/
        holder.wishEditImageBtn.setBackgroundResource(imageArr[position])

        holder.wishEditImageBtn.setOnClickListener {
          //  activity.showPicker()
            mListener?.onCardClick()

        }
        holder.dellImage.setOnClickListener {
            //todo презентер удаляет фото и говоит отобразить пикассо заглушку
            //  mAddGoodsPresenter.delPictureMyGood()

          //  privacyOptions
            //после успешного удаления скрываем   dellImage.visibility = View.GONE
        }
    }
    // создаем сам интерфейс и указываем метод и передаваемые им аргументы
// View на котором произошло событие и позиция этого View
    interface OnCardClickListener {
        fun onCardClick(/*view: View?, position: Int*/)
    }


    // метод-сеттер для привязки колбэка к получателю событий
    fun setOnCardClickListener(listener: OnCardClickListener) {
        mListener = listener
    }
}