package com.aoc4456.radarchart.component

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.aoc4456.radarchart.R
import petrov.kristiyan.colorpicker.ColorPicker

/**
 * タップするとカラーピッカーダイアログを表示するビュー
 *
 * 使い方：
 * 1. android:background="@{viewModel.color}"
 * 2. FragmentでsetOnChooseColorPicker を実装
 * 3. 戻り値のcolorを、ViewModelに通知する
 */
class ColorPickerView(context: Context, attrs: AttributeSet) :
    View(context, attrs),
    ColorPickerViewInput {

    override fun setOnChooseColorListener(activity: Activity, onChooseColor: (color: Int) -> Unit) {
        this.setOnClickListener {
            val colorPicker = ColorPicker(activity)
            colorPicker.disableDefaultButtons(true)
            colorPicker.setTitle(resources.getString(R.string.choose_a_color))

            val currentColor = (background as ColorDrawable).color
            colorPicker.setDefaultColorButton(currentColor)

            colorPicker.setOnFastChooseColorListener(object : ColorPicker.OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    onChooseColor(color)
                }

                override fun onCancel() {
                }
            })

            colorPicker.show()
        }
    }
}

interface ColorPickerViewInput {
    fun setOnChooseColorListener(activity: Activity, onChooseColor: (color: Int) -> Unit)
}
