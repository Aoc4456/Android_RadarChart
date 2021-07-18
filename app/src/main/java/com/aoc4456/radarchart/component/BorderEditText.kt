package com.aoc4456.radarchart.component

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.aoc4456.radarchart.R

class BorderEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    private val scale = resources.displayMetrics.density

    init {
        imeOptions = EditorInfo.IME_ACTION_DONE
        setEms(10)
        height = resources.getDimensionPixelSize(R.dimen.edit_text_height)
        background = ResourcesCompat.getDrawable(resources, R.drawable.border_radius, null)

        val padding = (8 * scale + 0.5f).toInt()
        setPadding(padding, 0, padding, 0)
    }
}
