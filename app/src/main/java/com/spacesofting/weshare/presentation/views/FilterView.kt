package com.spacesofting.weshare.presentation.views

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.TextView
import android.widget.ToggleButton

import com.pawegio.kandroid.d
import com.pawegio.kandroid.find
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.domain.common.utils.dp
import com.spacesofting.weshare.domain.common.utils.inflate
import kotlinx.android.synthetic.main.view_filter.view.*
import org.apmem.tools.layouts.FlowLayout

class FilterView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    interface OnOpeningFilterViewListener {
        fun onFilterViewOpen()
        fun onFilterViewClose()
    }

    interface OnChangeFilterListener {
        fun onChangeFilter(blockName: String, filterName: String, filterValue: Any, isSet: Boolean)
    }

    private val FILTER_TAG = "Filter_view"
    private var openingFilterViewListener: OnOpeningFilterViewListener? = null
    private var changeFilterListener: OnChangeFilterListener? = null
    private var selectedFiltersName = HashMap<String, ArrayList<String>>()
    private var selectedFiltersValue = HashMap<String, ArrayList<Any>>()
    private var filterBlocks = HashMap<String, ArrayList<String>>()
    private var buttonsList = HashMap<String, HashMap<String, ToggleButton>>()
    private var selectedFiltersCount = 0
    var isFilterViewOpen = false
        private set(value) {
            field = value
        }

    init {
        context.inflate(R.layout.view_filter, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        filterLayout.visible = false
    }

    private fun getToggleButton(blockName: String, filterName: String, filterValue: Any): ToggleButton {
        val themeWrapper = ContextThemeWrapper(context, R.style.Button_Toggle)
        val toggleButton = ToggleButton(themeWrapper, null, R.style.Button_Toggle)
        val padding = context.resources.getDimensionPixelSize(R.dimen.padding)
        val margin = 4.dp
        val lp = FlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        toggleButton.textOn = filterName
        toggleButton.textOff = filterName
        toggleButton.text = filterName
        toggleButton.setPadding(padding, padding, padding, padding)
        toggleButton.layoutParams = lp
        lp.marginStart = margin
        lp.marginEnd = margin
        lp.topMargin = margin
        lp.bottomMargin = margin

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedFiltersName[blockName]?.add(filterName)
                selectedFiltersValue[blockName]?.add(filterValue)
                selectedFiltersCount++
            } else {
                selectedFiltersName[blockName]?.remove(filterName)
                selectedFiltersValue[blockName]?.remove(filterValue)
                selectedFiltersCount--
            }
            changeFilterListener?.onChangeFilter(blockName, filterName, filterValue, isChecked)
        }

        buttonsList[blockName]?.put(filterName, toggleButton)
        return toggleButton
    }

//    private fun openAnimation(isOpen: Boolean) {
//        val constraintSet = ConstraintSet()
//        val targetVisibility = if (isOpen) View.GONE else View.VISIBLE
//        val targetHeight = if (isOpen) ConstraintSet.WRAP_CONTENT else 1
//
//        constraintSet.clone(this)
//        constraintSet.setVisibility(this.id, targetVisibility)
//        constraintSet.constrainHeight(R.id.filterLayout, targetHeight)
//        TransitionManager.beginDelayedTransition(this)
//        constraintSet.applyTo(this)
//    }

    fun openFilterView() {
        isFilterViewOpen = true
        openingFilterViewListener?.onFilterViewOpen()
        filterLayout.visible = true
    }

    fun closeFilterView() {
        isFilterViewOpen = false
        openingFilterViewListener?.onFilterViewClose()
        filterLayout.visible = false
    }

//    fun nestedScrollEnabled(isEnabled: Boolean) {
//        scrollView.isNestedScrollingEnabled = isEnabled
//    }

    fun setOnOpeningFilterViewListener(listener: OnOpeningFilterViewListener) {
        openingFilterViewListener = listener
    }

    fun removeOnOpeningFilterViewListener() {
        openingFilterViewListener = null
    }

    fun setOnChangeFilterListener(listener: OnChangeFilterListener) {
        changeFilterListener = listener
    }

    fun removeOnChangeFilterListener() {
        changeFilterListener = null
    }

    fun addFilterBlock(blockName: String, filtersName: ArrayList<String>, filtersValue: ArrayList<*>) {
        if (filtersName.size == filtersValue.size) {
            val block = context.inflate(R.layout.view_filter_block_item, container, false)
            container.addView(block)
            selectedFiltersName[blockName] = arrayListOf()
            selectedFiltersValue[blockName] = arrayListOf()
            buttonsList[blockName] = hashMapOf()
            filterBlocks[blockName] = filtersName

            for (i in 0 until filtersName.size){
                val toggleButton = getToggleButton(blockName, filtersName[i], filtersValue[i])
                block.find<FlowLayout>(R.id.filterContainer).addView(toggleButton)
                block.find<TextView>(R.id.blockTitle).text = blockName
            }
        } else {
            d(FILTER_TAG, "The number of filter names must match the number of filter values")
        }
    }

    fun getSelectedFiltersNameForBlock(blockName: String): ArrayList<String> {
        return if (selectedFiltersName.contains(blockName)) {
            selectedFiltersName[blockName]!!
        } else {
            d(FILTER_TAG, "Filter block name: \"$blockName\" not found")
            arrayListOf()
        }
    }

    fun <T> getSelectedFiltersValueForBlock(blockName: String): ArrayList<T> {
        return if (selectedFiltersValue.contains(blockName)) {
            selectedFiltersValue[blockName]!! as ArrayList<T>
        } else {
            d(FILTER_TAG, "Filter block name: \"$blockName\" not found")
            arrayListOf()
        }
    }

    fun selectFiltersForBlock(blockName: String, filterNames: ArrayList<String>) {
        filterNames.forEach {
            buttonsList.getValue(blockName).getValue(it).isChecked = true
        }
    }

    fun getSelectedFiltersCount(): Int {
        return selectedFiltersCount
    }
}