package com.spacesofting.weshare.mvp.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.pawegio.kandroid.displayWidth
import com.pawegio.kandroid.w
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.presentation.presenter.CompilationPresenter
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.dp
import com.spacesofting.weshare.utils.setHeight
import com.spacesofting.weshare.utils.setWidth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_compilation_wish.view.*
import java.util.*

class CompilationAdapter(val context: Context, val presenter: CompilationPresenter): RecyclerView.Adapter<CompilationAdapter.CompilationViewHolder>() {
    var dataset = ArrayList<Wish>()
    var marginW: Int = (16 * 2).dp
    var elementSize: Int = context.displayWidth - marginW

    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompilationViewHolder {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_compilation_wish, parent, false)
        return CompilationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompilationViewHolder, position: Int) {
        holder?.let {
            val item = dataset[position]
            bindWish(it, item)
        }
    }

    private fun bindWish(holder: CompilationViewHolder?, wish: Wish){
        holder?.let {
            it.wishImage.setHeight(elementSize)
            it.wishImage.setWidth(elementSize)
            loadWishImage (it.wishImage, wish)
           // it.wishPrice.text = wish.price?.toString()
            it.wishName.text = wish.name
            it.setLoading(wish.isLoading())

            it.add.setOnClickListener {
                holder.setLoading(true)
                wish.setLoading(true)
                presenter.addWish(wish)
            }
            it.wishImage.setOnClickListener { presenter.goToWish(wish) }
        }
    }

    private fun loadWishImage(wishImage: ImageView, wish: ListWishElement){
        if (wish.getImage() != null) {
            val firstImage = wish.getImage()!!
            val imageName = firstImage.name
            var imageColor = "#2EFFFFFF" // we need default color here because in some of cases we getting null color from server

            firstImage.color?.let {
                imageColor = "#2E${it.substring(1).replace(" ", "")}" // alpha 18%, remove spaces
            }

            imageName?.let {
                try {
                    Picasso.with(wishImage.context)
                        .load(ImageUtils.resolveImagePath(it))
                        .centerCrop()
                        .fit()
                        //.resize(elementSize, elementSize)
                        .placeholder(ColorDrawable(Color.parseColor(imageColor)))
                        .error(R.drawable.bg_image_load_error)
                        .into(wishImage)
                } catch (e: NumberFormatException) {
                    w("Incorrect color format $imageColor")
                }
            }
        } else {
            wishImage.setImageResource(R.drawable.bg_image_load_error)
        }
    }

    fun indexOf(wish: ListWishElement): Int = dataset.indexOf(wish)

    class CompilationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wishImage: ImageView = itemView.wishImage
        val wishName: TextView = itemView.wishName
        val wishPrice: TextView = itemView.wishPrice
        val add = itemView.addBtn

        fun setLoading(isLoading: Boolean){
            if(isLoading){
                val animation = AnimationUtils.loadAnimation(add.context, R.anim.alpha_fade)
                add.startAnimation(animation)
                add.isEnabled = false
            } else {
                add.clearAnimation()
                add.isEnabled = true
            }
        }
    }
}