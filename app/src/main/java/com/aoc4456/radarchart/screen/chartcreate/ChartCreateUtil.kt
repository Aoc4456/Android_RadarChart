package com.aoc4456.radarchart.screen.chartcreate

import android.graphics.Paint

object ChartCreateUtil {

    fun getValuedLabelList(labels: List<String>?, values: List<Int>?): List<String> {
        if (labels == null) return mutableListOf("")
        if (values == null) return labels

        val list = mutableListOf<String>()
        for (i in labels.indices) {
            val label = labels[i]
            val value = values[i]
            list.add(getValuedText(label, value))
        }
        return list
    }

    private fun getValuedText(label: String, value: Int): String {
        val space = getDiffSpace(label, value.toString())
        return "$label\n$space$value"
    }

    /**
     * 下段のテキストを上段の中央にあたる位置に配置するために、必要な分のスペースを返す
     */
    private fun getDiffSpace(label: String, value: String): String {
        val paint = Paint()

        val labelWidth = paint.measureText(label, 0, label.length)
        val valueWidth = paint.measureText(value, 0, value.length)
        val diff = labelWidth - valueWidth

        val spaceWidth = paint.measureText(" ", 0, " ".length)
        val spaceCount = ((diff / spaceWidth) / 2).toInt()

        var string = ""
        if (spaceCount > 0) {
            repeat(spaceCount) { string += " " }
        }
        return string
    }
}
