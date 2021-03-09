package com.spacesofting.weshare.mvp.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import moxy.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.RentItem
import com.spacesofting.weshare.mvp.presentation.presenter.IShootItPresenter
import com.spacesofting.weshare.mvp.presentation.view.IShootItView
import com.spacesofting.weshare.mvp.ui.adapter.ItemShootRentAdapter
import kotlinx.android.synthetic.main.fragment_irent.*

class IAmRentGoodsFragment : FragmentWrapper(),
    IShootItView {
    private var adapter: ItemShootRentAdapter? = null

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_irent
    }

    companion object {
        const val TAG = "IShootItFragment"

        fun newInstance(): IAmRentGoodsFragment {
            val fragment: IAmRentGoodsFragment =
                IAmRentGoodsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mIShootItPresenter: IShootItPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        initListItems()
    }

    private fun initListItems() {
        if (adapter == null) {
            adapter = ItemShootRentAdapter(activity!!)
        }
        adapter?.dataset?.clear()

        val mLayoutManager =
            androidx.recyclerview.widget.GridLayoutManager(activity, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(25), true))
        recyclerView.itemAnimator =
            androidx.recyclerview.widget.DefaultItemAnimator()

        val filterList = ArrayList<RentItem>()
        mIShootItPresenter.loadMyGoods()

        adapter?.dataset?.addAll(filterList)


        recyclerView.adapter = adapter
        recyclerView.layoutManager = mLayoutManager

        /* if (!presenter.isLoadedAllItems()) {
             Paginate.with(companiesList, this).build()
         }*/
    }

    inner class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }
}
