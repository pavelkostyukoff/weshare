package com.spacesofting.weshare.presentation.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.paginate.Paginate
import moxy.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.data.api.RespouncePublish
import com.spacesofting.weshare.presentation.common.FragmentWrapper
import com.spacesofting.weshare.presentation.mvp.model.RespounceDataMyAdverts
import com.spacesofting.weshare.presentation.presenter.IrentPresenter
import com.spacesofting.weshare.presentation.view.IrentView
import com.spacesofting.weshare.presentation.ui.adapter.ItemShootGoodsAdapter
import com.spacesofting.weshare.presentation.ui.adapter.ProductDiffUtilCallback
import kotlinx.android.synthetic.main.fragment_irent.*

class IAmShooltGoodsFragment(advert: RespouncePublish?) : FragmentWrapper(),
    Paginate.Callbacks,
    IrentView {
    private var adapter: ItemShootGoodsAdapter? = null

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_irent
    }
    val advert = advert

    companion object {
        const val TAG = "IRentFragment"

     /*   fun newInstance(): IRentFragment {
            val fragment: IRentFragment =
                IRentFragment(advert)
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }*/
    }

    @InjectPresenter
    lateinit var mIrentPresenter: IrentPresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        initAdapter()
    }

    private fun initAdapter() {

        adapter = ItemShootGoodsAdapter(mIrentPresenter)
        val mLayoutManager =
            StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
      //  recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(25), true))
       // mLayoutManager.isAutoMeasureEnabled = false

        recyclerView.layoutManager = mLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(8), true))
       // recyclerView.itemAnimator =
         //   androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.adapter = adapter

//        Paginate.with(compilationsList, this).build()
    }
     override fun delAdvertCOmplite() {
         adapter?.notifyDataSetChanged()
     }

    override fun editCOmplite() {
        adapter?.notifyDataSetChanged()
    }

    private fun listShow() {
        val mLayoutManager =
            androidx.recyclerview.widget.GridLayoutManager(activity, 2)
/*        recyclerView.layoutManager = mLayoutManager
            recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))*/
     /*   recyclerView.itemAnimator =
            androidx.recyclerview.widget.DefaultItemAnimator()*/
        recyclerView.adapter = adapter
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

    private fun initListItems(advert: List<RespounceDataMyAdverts>) {
        val clearArr =  ArrayList<RespounceDataMyAdverts>()

        if (adapter == null) {
            adapter = ItemShootGoodsAdapter(mIrentPresenter)
        } else {
        adapter?.dataset?.clear()
            advert.map {
                if (it.images?.size != 0){
                    clearArr.add(it)
                }
            }
        try {
            val productDiffUtilCallback =
                ProductDiffUtilCallback(adapter!!.dataset, clearArr)
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
            adapter!!.dataset = clearArr
            productDiffResult.dispatchUpdatesTo(adapter!!)
            //adapter!!.notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }
    }

    override fun setListAdverts(it: List<RespounceDataMyAdverts>?) {
        it?.let { it1 -> initListItems(it1) }
    }

    override fun onLoadMore() {
        TODO("Not yet implemented")
    }

    override fun isLoading(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasLoadedAllItems(): Boolean {
        TODO("Not yet implemented")
    }
}
