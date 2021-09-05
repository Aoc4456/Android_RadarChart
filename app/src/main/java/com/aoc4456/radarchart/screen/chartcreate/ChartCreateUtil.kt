package com.aoc4456.radarchart.screen.chartcreate

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
        return "$label \n $value"
    }
}
