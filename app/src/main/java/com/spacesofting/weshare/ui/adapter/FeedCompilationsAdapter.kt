package com.spacesofting.weshare.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.spacesofting.weshare.R
import com.makeramen.roundedimageview.RoundedImageView
import com.pawegio.kandroid.displayWidth
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.mvp.Compilation
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.ui.fragment.ui.fragment.presentation.presenter.FeedCompilationsPresenter
import com.spacesofting.weshare.utils.dp
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_category.view.*
import java.util.*

class FeedCompilationsAdapter(var context: Context, var presenter: FeedCompilationsPresenter): RecyclerView.Adapter<FeedCompilationsAdapter.CompilationViewHolder>(){
    private val MAX_ITEM_NUM    = 10
    private val wishItemMargin  = context.resources.getDimensionPixelSize(R.dimen.margin_half) * 2
    private var isFirstStart    = true
    private var itemNum         = 0
    private var viewPool        = RecyclerView.RecycledViewPool()
    val dataset                 = ArrayList<Compilation>()

    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CompilationViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.list_item_category, p0, false)
        return CompilationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompilationViewHolder, position: Int) {
        holder?.let{
            val item = dataset[position]
            val viewHolder = it

            if(item.state == Compilation.State.INACTIVE){
                viewHolder.root.visible = false
                return
            } else {
                viewHolder.root.visible = true
            }

            viewHolder.title.text = item.title
            viewHolder.subscribe.visible = !item.isFavorite
            viewHolder.unsubscribe.visible = item.isFavorite
            viewHolder.setLoading(item.isLoading())
            /*viewHolder.showMore.setOnClickListener { presenter.showCompilationDetails(item) }
            viewHolder.subscribe.setOnClickListener { presenter.subscribeCompilations(hashSetOf(item.id)) }
            viewHolder.unsubscribe.setOnClickListener { presenter.unsubscribeCompilation(item) }*/
            viewHolder.wishImage.setImageResource(R.drawable.wish_default_img)
            viewHolder.add.setOnClickListener {  }
            viewHolder.progress.visible = true

            if (!item.isInitialized) {
                item.isInitialized = true
/*
                presenter.loadCompilationsWishes(item, { list ->
                    item.wishList.addAll(list)
                    onUpdate(item)
                }, {})*/
            } else if(item.wishList.isNotEmpty()) {
                setWishList(viewHolder, item.wishList, item)
                updateItemView(viewHolder, item.wishList, item)
            }
        }
    }

    // todo подборка категорий
    private fun setWishList(holder: CompilationViewHolder, wishList: List<Wish>, item: Compilation){
        val adapter = CompilationsWishAdapter(wishList, item, this)

        holder.wishList.recycledViewPool = viewPool
        holder.wishList.visible = true
        holder.wishList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.wishList.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    // todo подборка вещей
    private fun updateItemView(holder: CompilationViewHolder, wishList: List<Wish>, item: Compilation){
        val size = wishList.size
        val num = if (itemNum <= size - 1) itemNum else (size - 1)
        val scrollPosition = if(itemNum > size) (itemNum - size) else itemNum
        val width = context.displayWidth - 16.dp * 2
        val height = context.resources.getDimensionPixelOffset(R.dimen.compilation_main_img_height)

        loadImage(holder.wishImage, wishList[num].getImage(), width, height, true)
        holder.wishName.text = wishList[num].name
        holder.wishCost.text = wishList[num].price?.toString()
        (holder.wishList.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(scrollPosition, wishItemMargin)

        holder.add.setOnClickListener {
            item.setLoading(true)
            presenter.addWish(wishList[num], item)
            holder.setLoading(item.isLoading())
            notifyItemChanged(holder.adapterPosition)
        }

        holder.root.setOnClickListener { presenter.showWish(wishList[num], item) }
        holder.progress.visible = false
    }

    //todo refactoring
    private fun onUpdate(compilation: Compilation){
        for (i in 0 until dataset.size){
            if (compilation.id == dataset[i].id && dataset[i].isInitialized){
                notifyItemChanged(i)
                break
            }
        }
    }

    fun loadImage(wishImg: RoundedImageView, img: Image?, width: Int, height: Int, isAnimation: Boolean = false){
        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_show)
        animation.duration = 600

        img?.let {
            var imageColor = "#2EFFFFFF"

            it.color?.let {
                imageColor = "#2E${it.replace(" ", "").substring(1)}"
            }

            it.name?.let {
                Picasso.with(wishImg.context)
                        .load(ImageUtils.resolveImagePath(it))
                        .resize(width, height)
                        .centerCrop()
                        .placeholder(ColorDrawable(Color.parseColor(imageColor)))
                        .error(R.drawable.bg_image_load_error)
                        .into(wishImg, object : Callback {
                            override fun onSuccess() {
                                if (isAnimation) {
                                    wishImg.startAnimation(animation)
                                }
                            }

                            override fun onError() {}
                        })
            }
        }
    }

    fun updateCompilationView(){
        // todo refactoring
        if (isFirstStart){
            isFirstStart = false
            return
        }

        if(itemNum < MAX_ITEM_NUM) {
            itemNum++
        } else {
            itemNum = 0
        }
        notifyDataSetChanged()
    }

    class CompilationViewHolder(item: View): RecyclerView.ViewHolder(item){
        val root: ConstraintLayout      = item.rootLayout
        val title: TextView             = item.title
        val showMore: LinearLayout      = item.showMore
        val subscribe: Button           = item.subscribe
        val unsubscribe: Button         = item.unsubscribe
        val wishImage: RoundedImageView = item.wishIamge
        val add: ImageButton            = item.addBtn
        val wishName: TextView          = item.wishName
        val wishCost: TextView          = item.wishCost
        val wishList: RecyclerView      = item.wishList
        val progress: FrameLayout       = item.progress

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