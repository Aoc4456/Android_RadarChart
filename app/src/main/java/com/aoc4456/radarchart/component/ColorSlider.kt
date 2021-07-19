package com.aoc4456.radarchart.component

import android.content.res.ColorStateList
import androidx.databinding.BindingAdapter
import com.google.android.material.slider.Slider

/**
 * [BindingAdapter]s for the MultiEditText
 */

@BindingAdapter("color")
fun changeColor(slider: Slider, color: Int) {
    slider.trackActiveTintList = ColorStateList.valueOf(color)
    slider.thumbTintList = ColorStateList.valueOf(color)
}
