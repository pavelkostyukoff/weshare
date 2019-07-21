package com.spacesofting.weshare.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.spacesofting.weshare.R
import com.spacesofting.weshare.mvp.Template
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.presentation.FeedPresenter
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.dp
import com.spacesofting.weshare.utils.setHeight
import com.squareup.picasso.Picasso
import java.util.*

class FeedAdapter(
    val presenter: FeedPresenter,
    val elementWidth: Int,
    t: ArrayList<ListWishElement>
): RecyclerView.Adapter<TemplateViewHolder>() {

    companion object {
        const val PICASSO_TAG = "picassoTag"

    }
val t = t
    //last template which we were trying to add
    var lastTemplate = -1
    var dataset = t//ArrayList<ListWishElement>()

    val imageMaxHeight = 280.dp

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
       /* if (holder == null) {
            return
        }
*/
        val t = Template()
        t.description = "SDkjasdmkjsnfmls"


        //val data = ListWishElement()
        //val dataset2 = ArrayList<ListWishElement>()
        dataset.add(t)

        //  val one = ListWishElement
       // dataset.add()
        val item = dataset[position]

        if (item.isBlocked == true)
        {
            return
        }

        if (item.getImage() != null) {
            val firstImage = item.getImage()!!
            val imageName = firstImage.name
            var imageColor = "#2EFFFFFF" // we need default color here because in some of cases we getting null color from server
            var height = elementWidth.div(firstImage.aspect!!).toInt()

            firstImage.color?.let {
                imageColor = "#2E${it.replace(" ", "").substring(1)}" // alpha 18% //fix " "
            }

            if (height > imageMaxHeight) {
                height  = imageMaxHeight
            }

            holder.templateImage.setHeight(height)

            imageName?.let {
                Picasso.with(holder.templateImage.context)
                        .load(ImageUtils.resolveImagePath(it))
                        .tag(PICASSO_TAG)
                        .centerInside()
                        .resize(elementWidth, height)
                        .placeholder(ColorDrawable(Color.parseColor(imageColor)))
                        .error(R.drawable.bg_image_load_error)
                        .into(holder.templateImage)
            }
        } else {
            holder.templateImage.setImageResource(R.drawable.bg_image_load_error)
        }

     //   holder.templatePrice.text = item.price?.toString()
        holder.templateName.text = item.name

        if (item.isLoading()) {
            val myFadeInAnimation = AnimationUtils.loadAnimation(holder.templateAddButton.context, R.anim.alpha_fade)
            holder.templateAddButton.startAnimation(myFadeInAnimation)
            holder.templateAddButton.isEnabled = false
            holder.templateAddButton.isActivated = true
        } else {
            holder.templateAddButton.isEnabled = true
            holder.templateAddButton.isActivated = false
            holder.templateAddButton.setOnClickListener {

                //check we trying to add wish second time
                val current = item.id
                val isForce = current == lastTemplate

                if (item is Template) {
                    //add force or not...
                    presenter.tryToAddWish(item)
                    item.setLoading(true)
                    notifyDataSetChanged()

                    if (isForce) {
                        //force call must be only one in a row
                        lastTemplate = -1
                    } else {
                        lastTemplate = current
                    }
                } else {
                    presenter.tryToAddWish(item)
                }
            }
        }

        if (item is Wish) {
            item.profile?.let {
                /*if (it.nick != null) {
                    holder.profileName.visibility = View.VISIBLE
                  //  holder.profileName.text = it.nick
                } else {
                    holder.profileName.visibility = View.GONE
                }*/

                holder.profileImage.visibility = View.VISIBLE
               /* if (it.img != null) {
                    Picasso.with(holder.profileImage.context)
                            .load(ImageUtils.resolveImagePath(it.img?.name!!))
                            .tag(PICASSO_TAG)
                            .resizeDimen(R.dimen.avatar_size, R.dimen.avatar_size)
                            .centerInside()
                            .into(holder.profileImage)
                } else {
                    holder.profileImage.setImageResource(R.drawable.ic_avatar_placeholder)
                }

                if (it.img == null && it.nick == null){
                    holder.profileImage.gone()
                }*/

                /*val clickListener = View.OnClickListener { view -> presenter.showProfile(it.phone) }
                //show profile on profile click
                holder.profileImage.setOnClickListener(clickListener)
                holder.profileName.setOnClickListener(clickListener)*/
            }
            //show pay button
            var amount = 0.0
            /*item.price?.amount?.let{
                amount = it.toDouble()
            }*/

            item.profile?.let {
                holder.templatePrice.visibility = if(amount > 0) View.VISIBLE else View.GONE
                //Todo тип пользователя арендодатель или арендатор
                /*if(it.userType == Profile.UserType.USER) {
                    holder.wallet.visibility = if (amount > 0 && item.id > 0) View.VISIBLE else View.GONE
                    holder.wallet.setOnClickListener { presenter.pay(item) }
                } else {
                    holder.wallet.visibility = View.GONE
                }*/
            }
        } else {
            //remove listener
            holder.profileImage.setOnClickListener { null }
            holder.profileName.setOnClickListener { null }

            //hide fields
            holder.profileImage.visibility = View.GONE
            holder.profileName.visibility = View.GONE

            //hide pay button
            holder.wallet.visibility =  View.GONE
        }

        //show detailed info by element
        holder.item.setOnClickListener { presenter.showWish(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_template, parent, false)
        return TemplateViewHolder(view)
    }

    fun pauseImgDownload(context: Context){
        Picasso.with(context)
                .pauseTag(PICASSO_TAG)
    }

    fun resumeImgDownload(context: Context){
        Picasso.with(context)
                .resumeTag(PICASSO_TAG)
    }
}