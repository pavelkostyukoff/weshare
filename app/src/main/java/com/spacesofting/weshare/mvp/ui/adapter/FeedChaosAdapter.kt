package com.spacesofting.weshare.mvp.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.pawegio.kandroid.displayWidth
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.Datum
import com.spacesofting.weshare.mvp.Image
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.presentation.presenter.FeedChaosPresenter
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.dp
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_category.view.*

class FeedChaosAdapter(var context: Context, var presenter: FeedChaosPresenter): androidx.recyclerview.widget.RecyclerView.Adapter<FeedChaosAdapter.CompilationViewHolder>(){
    private val MAX_ITEM_NUM    = 5
    private val wishItemMargin  = context.resources.getDimensionPixelSize(R.dimen.margin_half) * 2
    private var isFirstStart    = true
    private var itemNum         = 0
    private var viewPool        = androidx.recyclerview.widget.RecyclerView.RecycledViewPool()
    val dataset                 = ArrayList<Datum>()
    val wishList = ArrayList<Wish>()
    val w22 = Wish()


    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CompilationViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.list_item_category_chaos, p0, false)
        return CompilationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompilationViewHolder, position: Int) {
        holder?.let{
            val item = dataset[position]
            val viewHolder = it



    /*        if(item.state == Compilation.State.INACTIVE){
                viewHolder.root.visible = false
                return
            } else {*/
                viewHolder.root.visible = true
          //  }

            viewHolder.title.text = item.name
           /* viewHolder.subscribe.visible = !item.isFavorite
            viewHolder.unsubscribe.visible = item.isFavorite*/
         //   viewHolder.setLoading(item.isLoading())
            viewHolder.showMore.setOnClickListener { presenter.showCompilationDetails(item) }
          //  viewHolder.subscribe.setOnClickListener { presenter.subscribeCompilations(hashSetOf(item.id)) }
         //   viewHolder.unsubscribe.setOnClickListener { presenter.unsubscribeCompilation(item) }
          //  viewHolder.wishImage.setImageResource(R.drawable.wish_default_img)
            viewHolder.add.setOnClickListener {  }
            viewHolder.progress.visible = true

            //todo что то тас при иницализации чото там рисуем если есть
            //if (!item.isInitialized) {
               // item.isInitialized = true
              //  presenter.loadCompilationsWishes(item, { list ->
                    //todo тут происходит подгрузка вишей из подборки?
                    //item.wishList.addAll(list)
              //      onUpdate(item)
             //   }, {})
            //} else if(item.wishList.isNotEmpty()) {
            /*val arrOmg = ArrayList<Int>()
            arrOmg.add(R.drawable.icon_closed)
            arrOmg.add(R.drawable.icon_home)
            arrOmg.add(R.drawable.img_splash_bsq_1)
            arrOmg.add(R.drawable.icon_bissnes)
            arrOmg.add(R.drawable.icon_photo)
            arrOmg.add(R.drawable.icon_kids)
            arrOmg.add(R.drawable.icon_uikend)
            arrOmg.add(R.drawable.icon_model)
            arrOmg.add(R.drawable.icon_worker)*/


            val arrImg = ArrayList<Int>()
            arrImg.add(R.drawable.ic_big_car)
          //  arrImg.add(R.drawable.ic_building)
            arrImg.add(R.drawable.ic_car)
            arrImg.add(R.drawable.ic_dress)
            arrImg.add(R.drawable.ic_hobbit)
            arrImg.add(R.drawable.ic_hollidays)
            arrImg.add(R.drawable.ic_kids)
            arrImg.add(R.drawable.ic_tagik)
            arrImg.add(R.drawable.ic_tools)
           // viewHolder.wishImage.setImageResource(arrOmg.get(position))

            viewHolder.imageView.setImageResource(arrImg.get(position))

            w22.templateId = 0
            w22.compilationId = 0
            w22.description = "Wow it's worked"
            w22.name = item.name
           // w22.images() wishIamge
            if (item.tags?.isNotEmpty()!!)
            {
                w22.description = item.tags?.get(0)

            }
            wishList.clear()
            wishList.add(w22)

//todo возмодное причина тормозов
            https@ //ru.stackoverflow.com/questions/690916/тормозит-recyclerview-при-прокрутке-из-за-imageview-cardview
           //// RececlerView тормозит при пролистывании android asyncReportData
                updateItemView(viewHolder, wishList, item)
          //  }
        }
    }


    // todo подборка вещей вот тут могут быть лаги всязи с расчетом
    private fun updateItemView(holder: CompilationViewHolder, wishList: List<Wish>, item: Datum){
        val size = wishList.size
        val num = if (itemNum <= size - 1) itemNum else (size - 1)
        val scrollPosition = if(itemNum > size) (itemNum - size) else itemNum
        val width = context.displayWidth - 16.dp * 2
        val height = context.resources.getDimensionPixelOffset(R.dimen.compilation_main_img_height)

       loadImage(holder.wishImage, wishList[num].getImage(), width, height, true)
        holder.wishName.text = wishList[num].name
     //   holder.wishCost.text = wishList[num].price?.toString()
        (holder.wishList.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPositionWithOffset(scrollPosition, wishItemMargin)

        holder.add.setOnClickListener {
         //   item.setLoading(true)
            presenter.addWish(wishList[num], item)
            holder.setLoading(true) //todo false замена тест
            notifyItemChanged(holder.adapterPosition)
        }

        holder.root.setOnClickListener {

            presenter.showWish(wishList[num], item) }
        holder.progress.visible = false
    }

    //todo refactoring
    private fun onUpdate(compilation: Datum){
        for (i in 0 until dataset.size){
            /*if (compilation.id == dataset[i].id && dataset[i].isInitialized){
                notifyItemChanged(i)
                break
            }*/
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

    class CompilationViewHolder(item: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(item){
        val root: ConstraintLayout = item.rootLayout
        val title: TextView             = item.title
        val showMore: LinearLayout      = item.showMore
        val subscribe: Button           = item.subscribe
        val unsubscribe: Button         = item.unsubscribe
        val wishImage: RoundedImageView = item.wishIamge
        val imageView: ImageView = item.imageViewmini

        val add: ImageButton            = item.addBtn
        val wishName: TextView          = item.wishName
        val wishCost: TextView          = item.wishCost
        val wishList: androidx.recyclerview.widget.RecyclerView = item.wishList
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