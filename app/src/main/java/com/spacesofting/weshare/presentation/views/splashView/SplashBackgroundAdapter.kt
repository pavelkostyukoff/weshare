package com.spacesofting.weshare.presentation.views.splashbackgroundview

import android.animation.ObjectAnimator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.common.utils.dp
import com.spacesofting.weshare.presentation.common.utils.inflate
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList



/**
 * Created by bender on 06/06/2017.
 */
class SplashBackgroundAdapter: BaseAdapter() {

    val elementsDimmingAnimationDuration = 900L
    val dimmingRowsInitDelay = 500
    val dimmingRowsDelta = 300
    val dimmingElementsDelta = 100
    val dimmedElementAlpha = .1f
    val featuredElementElevation = 3.dp.toFloat()

    var prevLayoutId = -1
    var shouldIntroduceMagic = true
    val random = Random()
    var dimFlag = false

    val elementLayouts = intArrayOf(
        R.layout.item_splash_element_1,
        R.layout.item_splash_element_2,
        R.layout.item_splash_element_3,
        R.layout.item_splash_element_4
    )
    var bsqCounter = 0
    var sqCounter = 0
    var lnCounter = 0
    var ftCounter = 0
    val bigSquareImageResources = listOf (
        R.drawable.img_splash_bsq_1,
        R.drawable.img_splash_bsq_2,
        R.drawable.img_splash_bsq_3,
        R.drawable.img_splash_bsq_4,
        R.drawable.img_splash_bsq_5,
        R.drawable.img_splash_bsq_6,
        R.drawable.img_splash_bsq_7
    )
    val squareImageResources = listOf (
        R.drawable.img_splash_sq_1,
        R.drawable.img_splash_sq_2,
        R.drawable.img_splash_sq_3,
        R.drawable.img_splash_sq_4,
        R.drawable.img_splash_sq_5,
        R.drawable.img_splash_sq_6,
        R.drawable.img_splash_sq_7,
        R.drawable.img_splash_sq_8,
        R.drawable.img_splash_bsq_1,
        R.drawable.img_splash_bsq_2,
        R.drawable.img_splash_bsq_3,
        R.drawable.img_splash_bsq_4,
        R.drawable.img_splash_bsq_5,
        R.drawable.img_splash_bsq_6,
        R.drawable.img_splash_bsq_7
    )
    val fatImageResources = listOf (
        R.drawable.img_splash_ft_1,
        R.drawable.img_splash_ft_2,
        R.drawable.img_splash_ft_3,
        R.drawable.img_splash_ft_4,
        R.drawable.img_splash_ft_5,
        R.drawable.img_splash_ft_6,
        R.drawable.img_splash_ft_7,
        R.drawable.img_splash_ft_8
    )
    val longImageResources = listOf (
        R.drawable.img_splash_ln_1,
        R.drawable.img_splash_ln_2,
        R.drawable.img_splash_ln_3,
        R.drawable.img_splash_ln_4,
        R.drawable.img_splash_ln_5,
        R.drawable.img_splash_ln_6,
        R.drawable.img_splash_ln_7,
        R.drawable.img_splash_ln_8,
        R.drawable.img_splash_ln_9,
        R.drawable.img_splash_ln_10,
        R.drawable.img_splash_ln_11,
        R.drawable.img_splash_ln_12,
        R.drawable.img_splash_ln_13,
        R.drawable.img_splash_ln_14,
        R.drawable.img_splash_ln_15,
        R.drawable.img_splash_ln_16,
        R.drawable.img_splash_ft_1,
        R.drawable.img_splash_ft_2,
        R.drawable.img_splash_ft_3,
        R.drawable.img_splash_ft_4,
        R.drawable.img_splash_ft_5,
        R.drawable.img_splash_ft_6,
        R.drawable.img_splash_ft_7,
        R.drawable.img_splash_ft_8
    )

    val holdersCollection = ArrayList<SplashElementViewHolder>()

    init {
        Collections.shuffle(bigSquareImageResources)
        Collections.shuffle(squareImageResources)
        Collections.shuffle(fatImageResources)
        Collections.shuffle(longImageResources)

    }

    override fun getItem(position: Int) = Unit
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = 100

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        parent?.let {
            val view: View
            val holder: SplashElementViewHolder
            if (convertView != null) {
                view = convertView
                holder = convertView.tag as SplashElementViewHolder
            } else {
                prevLayoutId = getAlmostRandomLayout(prevLayoutId)
                view = it.context.inflate(prevLayoutId, parent, false)
                holder = SplashElementViewHolder(view, prevLayoutId)
                view.tag = holder
            }
            holdersCollection.add(holder)
            introduceImages(holder)
            if (!dimFlag) introduceMagic(holder, dimmingRowsInitDelay + dimmingRowsDelta * position)

            return view
        } ?: throw NullPointerException()
    }

    /**
     * return new random layout id from layouts array except we don't want it to be the same as previous
     */
    fun getAlmostRandomLayout(prevLayoutId: Int): Int {
        var layoutId = prevLayoutId
        while (layoutId == prevLayoutId) {
            layoutId = elementLayouts[random.nextInt(elementLayouts.size)]
        }
        return layoutId
    }

    fun introduceImages(viewHolder: SplashElementViewHolder) {
        val context = viewHolder.elements[0].context
        Picasso.with(context).load(getNextLnRes()).into(viewHolder.elements[0])
        Picasso.with(context).load(getNextLnRes()).into(viewHolder.elements[1])
        Picasso.with(context).load(getNextSqRes()).into(viewHolder.elements[2])
        when (viewHolder.layoutId) {
            R.layout.item_splash_element_1, R.layout.item_splash_element_4 -> run {
                Picasso.with(context).load(getNextFtRes()).into(viewHolder.elements[3])
                Picasso.with(context).load(getNextBsqRes()).into(viewHolder.elements[4])
            }
            R.layout.item_splash_element_2 -> run {
                Picasso.with(context).load(getNextSqRes()).into(viewHolder.elements[3])
                Picasso.with(context).load(getNextLnRes()).into(viewHolder.elements[4])
                Picasso.with(context).load(getNextLnRes()).into(viewHolder.elements[5])
            }
            R.layout.item_splash_element_3 -> run {
                Picasso.with(context).load(getNextSqRes()).into(viewHolder.elements[3])
                Picasso.with(context).load(getNextBsqRes()).into(viewHolder.elements[4])
            }
        }
    }

    fun introduceMagic(viewHolder: SplashElementViewHolder, startDelay: Int) {
        if (!shouldIntroduceMagic) return
        viewHolder.elements.forEachIndexed { index, imageView -> run {
            val parent = imageView.parent as androidx.cardview.widget.CardView
            parent.alpha = 0f

            val elementDelay = startDelay + index * dimmingElementsDelta
            parent.animate()
                .alpha(1f)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setStartDelay(elementDelay.toLong())
                .start()

        } }
    }

    fun dimBackground() {
        dimFlag = true
        holdersCollection
            .flatMap { it.elements }
            .map { it.parent as androidx.cardview.widget.CardView }
            .forEach {
                if (getMagicPortion()) {
                    it.animate().alpha(dimmedElementAlpha).setStartDelay(0).setDuration(elementsDimmingAnimationDuration).start()
                    ObjectAnimator.ofFloat(it, "cardElevation", 0f)
                } else {
                    it.animate().alpha(1f).setStartDelay(0).setDuration(elementsDimmingAnimationDuration).start()
                    ObjectAnimator.ofFloat(it, "cardElevation", featuredElementElevation)
                }
            }
    }

    fun stopIntroducingMagic() {
        shouldIntroduceMagic = false
    }

    fun getNextLnRes(): Int {
        if (lnCounter >= longImageResources.size) {
            lnCounter = 0
        }
        return longImageResources[lnCounter++]
    }

    fun getNextSqRes(): Int {
        if (sqCounter >= squareImageResources.size) {
            sqCounter = 0
        }
        return squareImageResources[sqCounter++]
    }

    fun getNextBsqRes(): Int {
        if (bsqCounter >= bigSquareImageResources.size) {
            bsqCounter = 0
        }
        return bigSquareImageResources[bsqCounter++]
    }

    fun getNextFtRes(): Int {
        if (ftCounter >= fatImageResources.size) {
            ftCounter = 0
        }
        return fatImageResources[ftCounter++]
    }

    /**
     * returns if the element should be featured or dimmed
     * if true, the element will be featured
     */
    fun getMagicPortion() = random.nextFloat() > .3f
}
