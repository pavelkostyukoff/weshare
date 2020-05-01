package com.spacesofting.weshare.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.pawegio.kandroid.d
import com.spacesofting.weshare.R


class AvatarImageBehavior(context: Context, attrs: AttributeSet?) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<ImageView>(context, attrs) {


    private val MIN_AVATAR_PERCENTAGE_SIZE = 0.3f
    private val EXTRA_FINAL_AVATAR_PADDING = 80

    private val TAG = "behavior"

    private var mCustomFinalYPosition: Float        = 0f
    private var mCustomFinalXPosition: Float        = 0f
    private var mCustomStartXPosition: Float        = 0f
    private var mCustomStartToolbarPosition: Float  = 0f
    private var mCustomStartHeight: Float           = 0f
    private var mCustomFinalHeight: Float           = 0f

    private var mAvatarMaxSize              = 0f
    private var mFinalLeftAvatarPadding     = 0f
    private var mStartXPosition             = 0
    private var mStartToolbarPosition       = 0f
    private var mStartYPosition             = 0
    private var mFinalYPosition             = 0
    private var mStartHeight                = 0
    private var mFinalXPosition             = 0
    private var mChangeBehaviorPoint        = 0f
    private val mContext = context

    init{
        attrs?.let {
           /* val a:TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior)
            mCustomFinalYPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0f)
            mCustomFinalXPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalXPosition, 0f)
            mCustomStartXPosition = a.getDimension(R.styleable.AvatarImageBehavior_startXPosition, 0f)
            mCustomStartToolbarPosition = a.getDimension(R.styleable.AvatarImageBehavior_startToolbarPosition, 0f)
            mCustomStartHeight = a.getDimension(R.styleable.AvatarImageBehavior_startHeight, 0f)
            mCustomFinalHeight = a.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0f)
            a.recycle()*/
        }

        mAvatarMaxSize = mContext.resources!!.getDimension(R.dimen.image_width)
        mFinalLeftAvatarPadding = mContext.resources!!.getDimension(R.dimen.spacing_normal)
    }

    override fun layoutDependsOn(
        parent: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {
        return dependency is Toolbar
    }

    override fun onDependentViewChanged(
        parent: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: ImageView,
        dependency: View
    ): Boolean {
        maybeInitProperties(child, dependency)

        val maxScrollDistance = mStartToolbarPosition.toInt()
        val expandedPercentageFactor = (dependency!!.y / maxScrollDistance)

        if(expandedPercentageFactor < mChangeBehaviorPoint){
            val heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint

            val distanceXToSubtract: Float = ((mStartXPosition - mFinalXPosition) * heightFactor)+ (child!!.height / 2)
            val distanceYToSubtract: Float = ((mStartYPosition - mFinalYPosition) * (1f - expandedPercentageFactor)) + (child.height / 2)

            val y = mStartYPosition - distanceYToSubtract

            child.x = mStartXPosition - distanceXToSubtract
            child.y = if(y < mFinalYPosition.toFloat()) (mFinalYPosition/2).toFloat() else y

            val heightToSubtract: Float = (mStartHeight - mCustomFinalHeight) * heightFactor

            val lp: androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams = child.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            lp.width = (mStartHeight - heightToSubtract).toInt()
            lp.height = (mStartHeight - heightToSubtract).toInt()
            child.layoutParams = lp
        } else {
            val distanceYToSubtract: Float = ((mStartYPosition - mFinalYPosition) * (1f - expandedPercentageFactor)) + (mStartHeight / 2)

            child!!.x = (mStartXPosition - child.width / 2).toFloat()
            child.y = mStartYPosition - distanceYToSubtract

            val lp: androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams = child.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            lp.width = mStartHeight
            lp.height = mStartHeight
            child.layoutParams = lp
        }
        return true
    }

    private fun maybeInitProperties(child: ImageView?, dependency: View?){
        if(mStartYPosition == 0){
            mStartYPosition = (dependency!!.y).toInt()
        }

        if(mFinalYPosition == 0){
            mFinalYPosition = (dependency!!.height / 2)
        }

        if(mStartHeight == 0){
            mStartHeight = child!!.height
        }

        if(mStartXPosition == 0){
            mStartXPosition = (child!!.x + (child.width / 2)).toInt()
        }

        if(mFinalXPosition == 0){
            mFinalXPosition = (mContext.resources!!.getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + mCustomFinalXPosition + (mCustomFinalHeight / 2)).toInt()
        }

        if(mStartToolbarPosition == 0f){
            mStartToolbarPosition = dependency!!.y
        }

        if(mChangeBehaviorPoint == 0f){
            mChangeBehaviorPoint = (child!!.height - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition))
        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = mContext.resources!!.getIdentifier("status_bar_height","dimen","android")

        if(resourceId > 0){
            result = mContext.resources!!.getDimensionPixelOffset(resourceId)
        }
        return result
    }
}