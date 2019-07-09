package com.spacesofting.weshare.views

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.spacesofting.weshare.utils.dp

/**
 * Created by bender on 30/06/2017.
 */
class SpacesItemDecoration: RecyclerView.ItemDecoration() {

    val sidePadding = 8.dp
    val bottomPadding = 10.dp
    var hasFullspannedItems = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect?.left = sidePadding
        outRect?.right = sidePadding
        outRect?.bottom = bottomPadding

        view?.let {
            // isFullSpan flag used by separator item, and in this case we don't need to correct padding (separator will handle this itself)
            if (!(it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan && !hasFullspannedItems) {
                // Add top margin only for the first item to avoid double space between items (two first items in first raw)
                if (parent?.getChildAdapterPosition(it) == 0 || parent?.getChildAdapterPosition(it) == 1) {
                    outRect?.top = bottomPadding
                }
            } else {
                // the list could contain several separators
                // to handle each of them we need to access to adapter and check list every time,
                // this is performance issue, so we suppose that first item should be the separator and use this
                // mark in the future
                hasFullspannedItems = true
            }
        }
    }

}