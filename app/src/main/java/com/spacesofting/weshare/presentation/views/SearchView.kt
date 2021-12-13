package com.spacesofting.weshare.presentation.views

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import com.pawegio.kandroid.layoutInflater
import com.pawegio.kandroid.runOnUiThread
import com.pawegio.kandroid.textWatcher
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import kotlinx.android.synthetic.main.view_search.view.*
import java.util.*

class SearchView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    interface OnChangeQueryListener {
        fun onChangedQuery(query: String)
        fun onQueryClear()
    }

    interface OnOpeningStateListener {
        fun onSearchClose()
        fun onSearchOpen()
    }

    private var timeout: Timer? = null
    private var queryListener: OnChangeQueryListener? = null
    private var openingStateListener: OnOpeningStateListener? = null
    private val SCHEDULE_TIMEOUT: Long = 1000
    private var listAdapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line)
    var isSearchOpen = false
        private set(value) {
            field = value
        }

    init {
        context.layoutInflater?.inflate(R.layout.view_search, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        btnCloseSearch.setOnClickListener {
            closeSearch()
        }

        btnClearQuery.setOnClickListener {
            clearSearch()
        }

        acQuery.textWatcher {
            this.afterTextChanged { editable ->
                if (!editable.isNullOrEmpty()) {
                    scheduleQuery(editable.toString())
                    btnClearQuery.visible = true
                } else {
                    btnClearQuery.visible = false
                    queryListener?.onQueryClear()
                    timeout?.cancel()
                }
            }
        }

        acQuery.setAdapter(listAdapter)
    }

    private fun scheduleQuery(query: String) {
        timeout?.cancel()
        timeout = Timer()

        val task = object: TimerTask() {
            override fun run() {
                runOnUiThread {
                    queryListener?.onChangedQuery(query)
                }
            }
        }

        timeout?.schedule(task, SCHEDULE_TIMEOUT)
    }

//    private fun showKeyboard() {
//        acQuery.requestFocus()
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(this.windowToken, 0)
//    }
//
//    private fun hideKeyboard() {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(this.windowToken, 0)
//    }

    private fun clearAutoCompleteList() {
        listAdapter.clear()
        listAdapter.notifyDataSetChanged()
    }

    fun openAnimation(isOpen: Boolean) {
        val constraintSet = ConstraintSet()
        val targetVisibility = if (isOpen) View.GONE else View.VISIBLE

        constraintSet.clone(rootLayout)
        constraintSet.setVisibility(R.id.rootLayout, targetVisibility)
        TransitionManager.beginDelayedTransition(rootLayout)
        constraintSet.applyTo(rootLayout)
    }

    fun clearSearch() {
        acQuery.text.clear()
        clearAutoCompleteList()
    }

    fun openSearch() {
        isSearchOpen = true
        rootLayout.visible = true
        rootLayout.visible = true
        openingStateListener?.onSearchOpen()
        acQuery.requestFocus()
//        showKeyboard()
    }

    fun closeSearch() {
        isSearchOpen = false
        rootLayout.visible = false
        rootLayout.visible = false
        openingStateListener?.onSearchClose()
        clearSearch()
//        hideKeyboard()
    }

    fun setOnChangeQueryListener(listener: OnChangeQueryListener) {
        queryListener = listener
    }

    fun removeOnChangeQueryListener() {
        queryListener = null
    }

    fun setOnOpeningStateListener(listener: OnOpeningStateListener) {
        openingStateListener = listener
    }

    fun removeOnOpeningStateListener() {
        openingStateListener = null
    }

    fun setAutoCompleteList(strList: ArrayList<String>, resetList: Boolean = true) {
        if (resetList) {
            listAdapter.clear()
        }

        listAdapter.addAll(strList)
        listAdapter.notifyDataSetChanged()
    }
}