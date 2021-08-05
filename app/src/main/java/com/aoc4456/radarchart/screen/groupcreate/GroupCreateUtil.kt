package com.aoc4456.radarchart.screen.groupcreate

import com.aoc4456.radarchart.datasource.database.ChartGroupLabel

object GroupCreateUtil {
    fun getExactlySizedTextList(textList: List<String>, numberOfItems: Int): List<String> {
        val diff = textList.size - numberOfItems
        if (diff > 0) {
            return textList.dropLast(diff)
        }
        return textList
    }

    fun getMaximumSizeTextList(labelList: List<ChartGroupLabel>): MutableList<String> {
        val tempList = defaultTextList.toMutableList()
        for (i in labelList.indices) {
            tempList[i] = labelList[i].text
        }
        return tempList
    }

    val defaultTextList = listOf("項目1", "項目2", "項目3", "項目4", "項目5", "項目6", "項目7", "項目8")
}
