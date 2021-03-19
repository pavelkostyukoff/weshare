package com.spacesofting.weshare.mvp.ui.widget

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.ResponceMyAdvertMaps
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.advert
import com.spacesofting.weshare.mvp.DatumRequast
import com.spacesofting.weshare.mvp.ui.adapter.BannerAdapterPhoto
import com.wangpeiyuan.cycleviewpager2.CycleViewPager2Helper
import com.wangpeiyuan.cycleviewpager2.indicator.DotsIndicator
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_add_goods.*

class ActionBottomDialogFragment : BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener,
    BannerAdapterPhoto.OnCardClickListener,
    View.OnClickListener {
    private var mListener: ItemClickListener? = null
    private var bannerItemsFake = ArrayList<String>()
    private var adapterBaner = BannerAdapterPhoto()
    var banners = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*   view.findViewById(R.id.textView).setOnClickListener(this);
    view.findViewById(R.id.textView2).setOnClickListener(this);
    view.findViewById(R.id.textView3).setOnClickListener(this);
    view.findViewById(R.id.textView4).setOnClickListener(this);*/

        val datum =   arguments?.getSerializable(DATA_KEY_STR) as DatumRequast
        datum.images?.map {image->
            image.url?.let { string ->
                banners.add(string)
            }
        }
        val nextItemVisiblePx = resources.getDimension(R.dimen.dialog_corner_radius)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.oval_radius)
        val dotsRadius = resources.getDimension(R.dimen.rect_corner_radius)
        val dotsPadding = resources.getDimension(R.dimen.rect_corner_radius)
        val dotsBottomMargin = resources.getDimension(R.dimen.rect_corner_radius)
        bannerBottom.viewPager2.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            CycleViewPager2Helper(bannerBottom)
                .setAdapter(adapterBaner)
                .setMultiplePagerScaleInTransformer(
                    nextItemVisiblePx.toInt(),
                    currentItemHorizontalMarginPx.toInt(),
                    0.1f
                )
                .setDotsIndicator(
                    dotsRadius,
                    Color.RED,
                    Color.GRAY,
                    dotsPadding,
                    0,
                    dotsBottomMargin.toInt(),
                    0,
                    DotsIndicator.Direction.CENTER
                )
                // .setAutoTurning(3000L)
                .build()

        }

        bannerBottom.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("Selected_Page", position.toString())
            }
        })
        titleDatum.text = datum.description
        adapterBaner.dataset = banners
        adapterBaner.notifyDataSetChanged()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is ItemClickListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(view: View) {
        val tvSelected = view as TextView
        mListener!!.onItemClick(tvSelected.text.toString())
        dismiss()
    }

    interface ItemClickListener {
        fun onItemClick(item: String?)
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        private const val DATA_KEY_STR = "string"
        fun getInstance(ooobb: DatumRequast): ActionBottomDialogFragment {
            val fragment = ActionBottomDialogFragment()
            /*   smsRegistration?.let {
                   val argument = Bundle()
                   argument.putSerializable(DATA_KEY, it)
                   fragment.arguments = argument
               }*/
            //todo сюда мы принимаем вещь из редактирвоания
                val argument = Bundle()
                argument.putSerializable(DATA_KEY_STR, ooobb)
                fragment.arguments = argument
            return fragment
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onCardClick() {
        TODO("Not yet implemented")
    }

    override fun onCardClickDelete(position: Int) {
        TODO("Not yet implemented")
    }
}