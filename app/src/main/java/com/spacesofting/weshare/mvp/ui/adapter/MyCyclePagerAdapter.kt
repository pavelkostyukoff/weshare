package com.spacesofting.weshare.mvp.ui.adapter

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.context
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.wangpeiyuan.cycleviewpager2.adapter.CyclePagerAdapter
import kotlinx.android.synthetic.main.image_picker_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyCyclePagerAdapter :  CyclePagerAdapter<MyCyclePagerAdapter.PagerViewHolder>() {
    var dataset = ArrayList<String>()

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
        val context = holder.dellImage.context
        //todo сценарий, если пишли из инета
        if (dataset[position].contains("http"))
        {

            val output =  StringBuilder().append(dataset[position].substring(0, 4))
                .append("s").append(dataset[position].substring(4, dataset[position].length)).toString()
            holder.wishEditImageView?.let { it ->
                Glide
                    .with(context)
                    .load(output)
                    .into(it)
            }

          /*  Picasso.with(context)
                .load(output)//"http://i.imgur.com/DvpvklR.png")//dataset[position])
                .error(R.drawable.wish_default_img)
                // .transform(RoundedCorners(radius))
                .into(holder.wishEditImageView,
                    object : Callback {
                        override fun onSuccess() {
                            output
                        }

                        override fun onError() {
                            output                        }
                    })*/
        }
        else {
            val f = File(dataset[position])

            Picasso.with(context)
                .load(f)
                .error(R.drawable.wish_default_img)
                // .transform(RoundedCorners(radius))
                .into(holder.wishEditImageView)
        }

       /* holder.wishEditImageView.let { it ->
            Glide
                .with(context)
                .load(file)

                .into(it)
        }*/
        holder.wishEditImageBtn.setOnClickListener {


            mListener?.onCardClick()
        }
        holder.dellImage.setOnClickListener {
            //todo презентер удаляет фото и говоит отобразить пикассо заглушку
              mListener?.onCardClickDelete(position)
          //  privacyOptions
            //после успешного удаления скрываем   dellImage.visibility = View.GONE
        }
        if (dataset.isNotEmpty()) {
            val test = dataset[0]
            if (test == " "){
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
        fun onCardClickDelete(/*view: View?,*/ position: Int)
    }


    // метод-сеттер для привязки колбэка к получателю событий
    fun setOnCardClickListener(listener: OnCardClickListener) {
        mListener = listener
    }
}