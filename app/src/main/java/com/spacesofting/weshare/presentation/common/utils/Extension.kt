package com.spacesofting.weshare.presentation.common.utils

import android.content.res.Resources

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()