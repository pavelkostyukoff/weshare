package com.spacesofting.weshare.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by bender on 05/06/2017.
 */
class LongFatImageView : ImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth/3*2)
        scaleType = ScaleType.CENTER_CROP
    }
}