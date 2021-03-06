package com.spacesofting.weshare.presentation.views.splashbackgroundview

import android.view.View
import android.widget.ImageView
import com.spacesofting.weshare.R

/**
 * Created by bender on 06/06/2017.
 */
class SplashElementViewHolder(itemView: View, val layoutId: Int) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    val elements = ArrayList<ImageView>()

    init {
        itemView.findViewById<ImageView>(R.id.image1)?.let { elements.add(it) }
        itemView.findViewById<ImageView>(R.id.image2)?.let { elements.add(it) }
        itemView.findViewById<ImageView>(R.id.image3)?.let { elements.add(it) }
        itemView.findViewById<ImageView>(R.id.image4)?.let { elements.add(it) }
        itemView.findViewById<ImageView>(R.id.image5)?.let { elements.add(it) }
        itemView.findViewById<ImageView>(R.id.image6)?.let { elements.add(it) }
    }

}
