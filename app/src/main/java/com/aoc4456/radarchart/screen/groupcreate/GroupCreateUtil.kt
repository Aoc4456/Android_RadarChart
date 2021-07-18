package com.aoc4456.radarchart.screen.groupcreate

object GroupCreateUtil {
    fun getExactlySizedTextList(textList: List<String>, numberOfItems: Int): List<String> {
        val diff = textList.size - numberOfItems
        if (diff > 0) {
            return textList.dropLast(diff)
        }
        return textList
    }
}
