package com.spacesofting.weshare.mvp.ui.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedImageView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Entity
import com.spacesofting.weshare.mvp.Wish
import kotlinx.android.synthetic.main.list_item_compilations_wish.view.*

class CompilationsWishAdapter(wishList: List<Wish>, val compilation: Entity, val parentAdapter: FeedCompilationsAdapter): androidx.recyclerview.widget.RecyclerView.Adapter<CompilationsWishAdapter.CompilationsWishViewHolder>() {
    var dataset =  ArrayList<Wish>()


    init {

        dataset.addAll(wishList)
        val wish = Wish()
        wish.templateId = 0
        wish.compilationId = 0
        wish.description = "Wow it's worked"
        wish.name = "Next"
        dataset.add(wish)

        val wish2 = wish.clone()

        dataset.add(wish2 as Wish)
        val wish3 = wish2.clone()
        dataset.add(wish3 as Wish)
        val wish4 = wish.clone()
        dataset.add(wish4 as Wish)
        val wish5 = wish.clone()
        dataset.add(wish5 as Wish)
        val wish6 = wish5.clone()
        dataset.add(wish6 as Wish)


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
            it.wishName.text = item.description

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

    class CompilationsWishViewHolder(item: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(item){
        val rootWish: ConstraintLayout = item.wishRootLayout
        val rootMore: ConstraintLayout = item.rootMoreLayout
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