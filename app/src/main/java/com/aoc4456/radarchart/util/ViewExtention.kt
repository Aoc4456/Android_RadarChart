package com.aoc4456.radarchart.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

/**
 * Viewにマージンをセットします
 * Viewが親ViewGroupにアタッチされた後に呼び出してください
 */
fun View.setMargin(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom
) {
    if (layoutParams == null) {
        throw IllegalStateException("This View is not attached to ViewGroup")
    }
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}
