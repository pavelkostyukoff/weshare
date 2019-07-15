package com.spacesofting.weshare.ui.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedImageView
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.BaseListItem
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Wish
import kotlinx.android.synthetic.main.list_item_compilations_wish.view.*

class CompilationsWishAdapter(wishList: List<Wish>, val compilation: Compilation, val parentAdapter: FeedCompilationsAdapter): RecyclerView.Adapter<CompilationsWishAdapter.CompilationsWishViewHolder>() {
    var dataset =  ArrayList<Wish>()

    init {
        dataset.addAll(wishList)
       // dataset.add(MoreItem())
    }

    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CompilationsWishViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.list_item_compilations_wish, p0, false)
        return CompilationsWishViewHolder(view)
    }

    override fun onBindViewHolder(p0: CompilationsWishViewHolder, position: Int) {
        p0?.let {
            val item = dataset[position]
            val cost = it.wishCost
            val presenter = parentAdapter.presenter
            val width = it.wishImg.context.resources.getDimensionPixelSize(R.dimen.compilations_wish_image_size)
            val height = width

           /* if(item is Wish) {
                parentAdapter.loadImage(it.wishImg, item.getImage(), width, height)
                cost.text = "0.0"
                item.price?.let {
                    cost.text = it.toString()
                }

                it.rootWish.visible = true
                it.rootMore.visible = false
                it.wishName.text = item.name
                it.setLoading(item.isLoading())
                it.rootWish.setOnClickListener { presenter.showWish(item, compilation) }
                it.add.setOnClickListener {
                    presenter.addWish(item, null, this)
                    item.setLoading(true)
                    notifyItemChanged(position)
                }
            } else if(item is MoreItem) {
                it.rootWish.visible = false
                it.rootMore.visible = true
                it.rootMore.setOnClickListener {
                    presenter.showCompilationDetails(compilation)
                }
            }*/
        }
    }

    class CompilationsWishViewHolder(item: View): RecyclerView.ViewHolder(item){
        val rootWish: ConstraintLayout  = item.wishRootLayout
        val rootMore: ConstraintLayout  = item.rootMoreLayout
        val wishImg: RoundedImageView   = item.wishImage
        val wishName: TextView          = item.wishName
        val wishCost: TextView          = item.wishCost
        val add: ImageButton            = item.addBtn

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

    class MoreItem : BaseListItem {
        override fun isLoading(): Boolean {
            return false
        }

        override fun setLoading(value: Boolean) {

        }
    }
}