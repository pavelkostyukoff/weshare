package com.spacesofting.weshare.mvp.ui.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.paginate.Paginate
import moxy.presenter.InjectPresenter
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.ResponcePublish
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.model.RespounceDataMyAdverts
import com.spacesofting.weshare.mvp.presentation.presenter.IrentPresenter
import com.spacesofting.weshare.mvp.presentation.view.IrentView
import com.spacesofting.weshare.mvp.ui.adapter.FeedCompilationsAdapter
import com.spacesofting.weshare.mvp.ui.adapter.ItemThingRentAdapter
import kotlinx.android.synthetic.main.fragment_irent.*

class IRentFragment(advert: ResponcePublish?) : FragmentWrapper(),
    Paginate.Callbacks,
    IrentView {
    private var adapter: ItemThingRentAdapter? = null

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
     //   val test = RespounceDataMyAdverts()
     //   val arr = ArrayList<RespounceDataMyAdverts>()

       // test.images = advert.images
      //  advert?.let { arr.add(it) }
      //  initListItems(arr)

    }

    private fun initAdapter() {
        adapter = ItemThingRentAdapter(mIrentPresenter)
        val mLayoutManager =
            androidx.recyclerview.widget.GridLayoutManager(activity, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recyclerView.itemAnimator =
            androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = mLayoutManager
//        Paginate.with(compilationsList, this).build()
    }
     override fun delAdvertCOmplite() {
         mIrentPresenter.getMyAdvert()
     }

    override fun editCOmplite() {
        mIrentPresenter.getMyAdvert()
    }

    private fun listShow() {
        val mLayoutManager =
            androidx.recyclerview.widget.GridLayoutManager(activity, 2)
        recyclerView.layoutManager = mLayoutManager
            recyclerView.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recyclerView.itemAnimator =
            androidx.recyclerview.widget.DefaultItemAnimator()
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

    private fun initListItems(advert: ArrayList<RespounceDataMyAdverts>) {
        if (adapter == null) {
            adapter = ItemThingRentAdapter(mIrentPresenter)
        }
        adapter?.dataset?.clear()
        try {
            adapter!!.dataset = advert
            adapter?.notifyDataSetChanged()
        }
        catch (e:Exception){

        }

    }

    override fun setListAdverts(it: ArrayList<RespounceDataMyAdverts>?) {
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
