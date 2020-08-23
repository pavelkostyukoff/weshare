package com.spacesofting.weshare.mvp.ui.widget

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.pawegio.kandroid.find
import com.spacesofting.weshare.R

class IOSSheetDialog(private val context: Context) {
    interface OnSelectedItemListener {
        fun onSelectedItem(btnName: String)
    }

    private var dialog: AlertDialog? = null
    private var dialogView: View? = null
    private var onSelectedListener: OnSelectedItemListener? = null
    private var itemBtnCount = 0

    init {
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_chose, null)
        dialogView?.find<Button>(R.id.btnClose)?.setOnClickListener { dialog?.dismiss() }
        dialogView?.find<ConstraintLayout>(R.id.wrapper)?.clipToOutline = true

        dialog = AlertDialog.Builder(context, R.style.AlertDialog)
                .setView(dialogView)
                .create()

        val params = dialog?.window?.attributes
        params?.gravity = Gravity.BOTTOM
    }

    private fun getItemBtn(btnName: String, textColorResId: Int = -1): Button {
        val themeWrapper = ContextThemeWrapper(context, R.style.IOSSheetDialog_Item)
        val btn = Button(themeWrapper, null, R.style.IOSSheetDialog_Item)
        val padding = context.resources.getDimensionPixelSize(R.dimen.padding)

        if (textColorResId != -1) {
            btn.setTextColor(ContextCompat.getColor(context, textColorResId))
        }

        btn.id = itemBtnCount.inc()
        btn.text = btnName
        btn.setPadding(0, padding, 0, padding)
        btn.setOnClickListener {
            onSelectedListener?.onSelectedItem(btnName)
            dialog?.dismiss()
        }
        return btn
    }

    fun setTitle(str: String): IOSSheetDialog {
        dialogView?.find<TextView>(R.id.tvTitle)?.text = str
        return this
    }

    fun setTitle(strResId: Int): IOSSheetDialog {
        dialogView?.find<TextView>(R.id.tvTitle)?.text = context.getString(strResId)
        return this
    }

    fun addButton(btnName: String, textColorResId: Int = -1): IOSSheetDialog {
        val btn = getItemBtn(btnName, textColorResId)
        dialogView?.findViewById<LinearLayout>(R.id.container)?.addView(btn)
        return this
    }

    fun addButton(btnNameResId: Int, textColorResId: Int = -1): IOSSheetDialog {
        val btn = getItemBtn(context.getString(btnNameResId), textColorResId)
        dialogView?.findViewById<LinearLayout>(R.id.container)?.addView(btn)
        return this
    }

    fun setOnSelectedListener(listener: OnSelectedItemListener): IOSSheetDialog {
        onSelectedListener = listener
        return this
    }

    fun setCloseButtonName(name: String): IOSSheetDialog {
        dialogView?.find<Button>(R.id.btnClose)?.text = name
        return this
    }

    fun setCloseButtonName(nameResId: Int): IOSSheetDialog {
        dialogView?.find<Button>(R.id.btnClose)?.text = context.getString(nameResId)
        return this
    }

    fun show() {
        if (itemBtnCount > 0) {
            dialog?.show()
        }
    }
}