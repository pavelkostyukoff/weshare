package com.spacesofting.weshare.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.makeramen.roundedimageview.RoundedImageView
import com.pawegio.kandroid.displayWidth
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.data.api.Entity
import com.spacesofting.weshare.domain.common.ApplicationWrapper
import com.spacesofting.weshare.domain.common.CategotiesImage.*
import com.spacesofting.weshare.domain.common.ScreenPool
import com.spacesofting.weshare.mvp.Datum
import com.spacesofting.weshare.mvp.DatumRequast
import com.spacesofting.weshare.mvp.Image
import com.spacesofting.weshare.presentation.presenter.FeedCompilationsPresenter
import com.spacesofting.weshare.domain.common.utils.dp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_category.view.*

class FeedCompilationsAdapter(var context: Context, var presenter: FeedCompilationsPresenter) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FeedCompilationsAdapter.CompilationViewHolder>() {
    private val MAX_ITEM_NUM = 5
    private val wishItemMargin = context.resources.getDimensionPixelSize(R.dimen.margin_half) * 2
    private var isFirstStart = true
    private var itemNum = 0
    private var viewPool = androidx.recyclerview.widget.RecyclerView.RecycledViewPool()
    val dataset = ArrayList<Entity>()
    private val wishList = ArrayList<DatumRequast>()
    private val datum = DatumRequast()
    private val datum2 = DatumRequast()
    val router = ApplicationWrapper.instance.getRouter()


    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CompilationViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_item_category, p0, false)
        return CompilationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompilationViewHolder, position: Int) {
        holder.let { it ->
            val item = dataset[position]
            val viewHolder = it
            viewHolder.root.visible = true
            viewHolder.title.text = item.name
            /* viewHolder.subscribe.visible = !item.isFavorite
             viewHolder.unsubscribe.visible = item.isFavorite*/
            //   viewHolder.setLoading(item.isLoading())
            viewHolder.showMore.setOnClickListener { presenter.showCompilationDetails(item) }
            //  viewHolder.subscribe.setOnClickListener { presenter.subscribeCompilations(hashSetOf(item.id)) }
            //   viewHolder.unsubscribe.setOnClickListener { presenter.unsubscribeCompilation(item) }
            //  viewHolder.wishImage.setImageResource(R.drawable.wish_default_img)
            viewHolder.add.setOnClickListener { }
            viewHolder.goToCategory.setOnClickListener {
                router.navigateTo(ScreenPool.SINGLE_CATEGORY_FRAGMENT)
            }

            viewHolder.showGoods.setOnClickListener {
                //todo передать id вещи
               val test =  presenter.item8Maps.get(dataset[position].id)
                if (test != null){
                    if (test.isNotEmpty()) {
                        router.navigateTo(ScreenPool.SHOW_ADVERT_FRAGMENT, test[0])
                    }
                }
            }
            viewHolder.progress.visible = true
            val arrImg = ArrayList<Int>()
            wishList.clear()
            presenter.entitiesListPresenter?.map { entity ->
                var icon = 0
                when (entity.id) {
                    KINDS_ALL.number -> {
                        icon = R.drawable.kids
                        // тут достаем список презентера и отправляем по id
                    }
                    ELECTRONICS_ALL.number -> {
                        icon = R.drawable.transport
                    }
                    CLOUSED_ODEZDA_ALL.number -> {
                        icon = R.drawable.clouse
                    }
                    BUILDING_ALL.number -> {
                        icon = R.drawable.nedviga
                    }
                    OTDIH.number -> {
                        icon = R.drawable.rest_otdih
                    }
                    OBORUDOVANIE_ALL.number -> {
                        icon = R.drawable.oborudovanie_stroyka
                    }
                    PROCHEE.number -> {
                        icon = R.drawable.tools_instruments
                    }
                    ELECTRONICS_ALL.number -> {
                        icon = R.drawable.electronix
                    }
                    HOBBI_ALL.number -> {
                        icon = R.drawable.sports
                    }
                    else -> {
                        icon = R.drawable.bg_image_load_error
                    }
                }
                arrImg.add(icon)
            }
            //todo мы тут собираем вещи в карту согласно их id

            if (presenter.item8Maps.get(dataset[position].id) != null) {
                wishList.clear()
                val listUpdate = presenter.item8Maps.get(dataset[position].id)
                if (!listUpdate?.isEmpty()!!) {
                    listUpdate.map {
                        if ( it.categoryId.equals(dataset[position].id)){
                            wishList.addAll(listUpdate)
                        }
                    }
                    //setWishList(viewHolder, presenter.item8Maps.get(entity.id.toString())!!, item)
                }
            }
            viewHolder.imageView.setImageResource(arrImg[position])
            if (wishList.isEmpty()) {
                datum.categoryId = "test"
                datum.createdAt = "test"
                datum.description = "Wow it's worked"
                datum.description = item.name

                datum2.categoryId = "test"
                datum2.createdAt = "test"
                datum2.description = "Wow it's worked"
                datum2.description = item.name

                wishList.clear()
                wishList.add(datum)
                wishList.add(datum2)
            }


            //todo тут на полняется каждый список

            setWishList(viewHolder, wishList, item)
            updateItemView(viewHolder, wishList, item)

//todo возмодное причина тормозов

            //ru.stackoverflow.com/questions/690916/тормозит-recyclerview-при-прокрутке-из-за-imageview-cardview
            //// RececlerView тормозит при пролистывании android asyncReportData

            //  }
        }
    }

    // todo подборка категорий ADAPTER ВНУТРИ АДАПТЕРА вот сюда и передаем 8 items
    private fun setWishList(
        holder: CompilationViewHolder,
        wishList: List<DatumRequast>,
        item: Entity
    ) {
        val adapter = CompilationsWishAdapter(wishList, item, this)
        // holder.wishList.recycledViewPool = viewPool
        holder.wishList.visible = true
        holder.wishList.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
        holder.wishList.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    // todo подборка вещей вот тут могут быть лаги всязи с расчетом
    private fun updateItemView(
        holder: CompilationViewHolder,
        wishList: List<DatumRequast>,
        item: Entity
    ) {
        val size = wishList.size
        val num = if (itemNum <= size - 1) itemNum else (size - 1)
        val scrollPosition = if (itemNum > size) (itemNum - size) else itemNum
        val width = context.displayWidth - 16.dp * 2
        val height = context.resources.getDimensionPixelOffset(R.dimen.compilation_main_img_height)

        loadImage(holder.showGoods, wishList[num].getImage(), width, height, true)
        holder.categoryName.text = dataset[num].name
        //   holder.wishCost.text = wishList[num].price?.toString()
        (holder.wishList.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPositionWithOffset(
            scrollPosition,
            wishItemMargin
        )

        holder.add.setOnClickListener {
            //   item.setLoading(true)
            //todo добавить виш себе в ищбранное presenter.addWish(wishList[num], item)
            holder.setLoading(true) //todo false замена тест
            notifyItemChanged(holder.adapterPosition)
        }

        holder.root.setOnClickListener {
            //todo показать вещь  presenter.showWish(wishList[num], item)
        }
        holder.progress.visible = false
    }

    //todo refactoring
    private fun onUpdate(compilation: Datum) {
        for (i in 0 until dataset.size) {
            /*if (compilation.id == dataset[i].id && dataset[i].isInitialized){
                notifyItemChanged(i)
                break
            }*/
        }
    }

    fun loadImage(
        wishImg: RoundedImageView,
        img: Image?,
        width: Int,
        height: Int,
        isAnimation: Boolean = false
    ) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_show)
        animation.duration = 600

        img?.let {
            var imageColor = "#2EFFFFFF"
/*
            it.color?.let {
                imageColor = "#2E${it.replace(" ", "").substring(1)}"
            }*/
             Picasso.Builder(wishImg.context)
            .listener { picasso, uri, exception ->
                exception.printStackTrace()
            }
            .build().load(it.url).into(wishImg)
/*            it.url?.let {
                Picasso.with(wishImg.context)
                    .load(it)
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

                        override fun onError() {
                            TODO("Not yet implemented")
                        }

                        fun onError(e: Exception?) {}
                    })
            }*/
        }
    }

    fun updateCompilationView() {
        // todo refactoring
        if (isFirstStart) {
            isFirstStart = false
            return
        }

        if (itemNum < MAX_ITEM_NUM) {
            itemNum++
        } else {
            itemNum = 0
        }
        notifyDataSetChanged()
    }

    class CompilationViewHolder(item: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(item) {
        val root: ConstraintLayout = item.rootLayout
        val title: TextView = item.title
        val showMore: LinearLayout = item.showMore
        val goToCategory: Button = item.goToCategory
        val unsubscribe: Button = item.unsubscribe
        val showGoods: RoundedImageView = item.showGoods
        val imageView: ImageView = item.imageViewmini

        val add: ImageButton = item.addBtn
        val categoryName: TextView = item.categoryName
        val wishCost: TextView = item.wishCost
        val wishList: androidx.recyclerview.widget.RecyclerView = item.wishList
        val progress: FrameLayout = item.progress

        fun setLoading(isLoading: Boolean) {
            if (isLoading) {
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