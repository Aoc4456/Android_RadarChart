package com.aoc4456.radarchart.screen.groupcreate

import com.aoc4456.radarchart.datasource.database.ChartGroupLabel
import java.util.*

object GroupCreateUtil {
    fun getExactlySizedTextList(textList: List<String>, numberOfItems: Int): List<String> {
        val diff = textList.size - numberOfItems
        if (diff > 0) {
            return textList.dropLast(diff)
        }
        return textList
    }

    fun getMaximumSizeTextList(labelList: List<ChartGroupLabel>): MutableList<String> {
        val tempList = getDefaultTextList().toMutableList()
        for (i in labelList.indices) {
            tempList[i] = labelList[i].text
        }
        return tempList
    }

    fun getDefaultTextList(): List<String> {
        val locale = Locale.getDefault()
        val language = locale.language
        return if (language == "ja") {
            listOf("項目1", "項目2", "項目3", "項目4", "項目5", "項目6", "項目7", "項目8")
        } else {
            listOf("Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8")
        }
    }
}
