package com.spacesofting.weshare.mvp.presentation.views

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.util.AttributeSet

class StaggeredRecyclerView: androidx.recyclerview.widget.RecyclerView {

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
        layoutManager =
            androidx.recyclerview.widget.StaggeredGridLayoutManager(2, VERTICAL)
        (layoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager).gapStrategy = androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

      /*  if(getItemDecorationAt(0) == null) {
            addItemDecoration(SpacesItemDecoration())
        }*/
    }
    fun topPadding(paddingPixels: Int) {
        setPadding(paddingLeft, paddingPixels, paddingRight, paddingBottom)
    }
}