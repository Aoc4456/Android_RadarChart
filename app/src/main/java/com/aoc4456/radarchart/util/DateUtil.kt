package com.aoc4456.radarchart.util

import android.content.Context
import java.util.*

object DateUtil {
    fun getLocalDateStringFromMilliSecond(context: Context, milliSecond: Long): String {
        val dateFormat = android.text.format.DateFormat.getDateFormat(context)
        return dateFormat.format(Date(milliSecond))
    }
}
