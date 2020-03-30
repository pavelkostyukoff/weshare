package com.spacesofting.weshare.mvp.presentation.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet

class StaggeredRecyclerView: RecyclerView {

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    fun init() {
        layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        (layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

      /*  if(getItemDecorationAt(0) == null) {
            addItemDecoration(SpacesItemDecoration())
        }*/
    }
    fun topPadding(paddingPixels: Int) {
        setPadding(paddingLeft, paddingPixels, paddingRight, paddingBottom)
    }
}