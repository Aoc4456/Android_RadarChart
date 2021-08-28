package com.aoc4456.radarchart.util

import android.content.Context
import android.content.res.Configuration
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import timber.log.Timber
import kotlin.math.min

fun calcSpanCountBasedOnScreenSize(context: Context, type: CollectionType): Int {
    val orientation = context.resources.configuration.orientation
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    Timber.d("画面幅 = $dpWidth")
    when (type) {
        CollectionType.LIST -> {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                return if (dpWidth < 600) {
                    1
                } else {
                    2
                }
            } else {
                2
            }
        }
        CollectionType.GRID -> {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                val base = (dpWidth / 120).toInt()
                min(base, 4)
            } else {
                val base = (dpWidth / 150).toInt()
                min(base, 5)
            }
        }
    }
}
