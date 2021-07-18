package com.aoc4456.radarchart.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.util.setMargin

/**
 * 複数個の項目名を入力するためのView
 * EditTextたちをホストする
 *
 * 使い方
 * 1. xmlで app:textList="@{viewmodel.textList}"
 * 2. textListが更新されると、Viewの設置とテキストのセットが行われる
 */
class MultiEditText(context: Context, private val attrs: AttributeSet) :
    LinearLayout(context, attrs),
    MultiEditTextInput {

    private val editTextList = mutableListOf<EditText>()

    init {
        orientation = VERTICAL
    }

    override fun changeNumberOfItems(textList: List<String>) {
        val diff = kotlin.math.abs(editTextList.size - textList.size)
        if (diff == 0) return

        if (editTextList.size < textList.size) {
            appendItems(diff, textList)
        } else {
            removeItems(diff)
        }
    }

    private fun appendItems(appendNum: Int, textList: List<String>) {
        val currentNum = editTextList.size
        for (i in currentNum until (currentNum + appendNum)) {
            val editText = createEditText(textList[i])
            placeView(editText)
        }
    }

    private fun removeItems(removeNum: Int) {
        for (i in 0 until removeNum) {
            val removeEditText = editTextList.last()
            editTextList.remove(removeEditText)
            removeView(removeEditText)
        }
    }

    private fun createEditText(text: String?): EditText {
        val editText = BorderEditText(context, attrs)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.height = resources.getDimensionPixelSize(R.dimen.edit_text_height)
        text?.let {
            editText.setText(it)
        }
        return editText
    }

    private fun placeView(view: EditText) {
        editTextList.add(view)
        addView(view)

        val scale = resources.displayMetrics.density
        val topMargin = (8 * scale + 0.5f).toInt()
        view.setMargin(top = topMargin)
    }
}

interface MultiEditTextInput {
    fun changeNumberOfItems(textList: List<String>)
}

interface MultiEditTextOutput {
    fun onEndEditingMultiEditText(index: Int, text: String)
}

/**
 * [BindingAdapter]s for the MultiEditText
 */

@BindingAdapter("app:textList")
fun changeNumberOfItems(multiEditText: MultiEditText, textList: List<String>) {
    multiEditText.changeNumberOfItems(textList)
}
