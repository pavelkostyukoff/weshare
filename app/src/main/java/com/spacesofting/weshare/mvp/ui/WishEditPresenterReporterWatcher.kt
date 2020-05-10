package com.spacesofting.weshare.mvp.ui

import android.text.Editable
import android.text.TextWatcher
import com.spacesofting.weshare.mvp.presentation.presenter.AddGoodsPresenter

class WishEditPresenterReporterWatcher(val presenter: AddGoodsPresenter, val field: AddGoodsPresenter.Field): TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        if (s != null) {
            presenter.fieldChanged(s.toString(), field)
        } else {
            presenter.fieldChanged(null, field)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}