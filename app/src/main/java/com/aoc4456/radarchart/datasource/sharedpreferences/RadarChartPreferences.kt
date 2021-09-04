package com.aoc4456.radarchart.datasource.sharedpreferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RadarChartPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * アプリ起動回数 (規定回数到達で In App Review 表示)
     */

    fun incrementAppLaunchCount() {
        preferences.edit().putInt(KEY_APP_LAUNCH_COUNT, loadAppLaunchCount() + 1).apply()
    }

    fun resetAppLaunchCount() {
        preferences.edit().putInt(KEY_APP_LAUNCH_COUNT, 0).apply()
    }

    fun loadAppLaunchCount(): Int {
        return preferences.getInt(KEY_APP_LAUNCH_COUNT, 0)
    }

    /**
     * チャート一覧 リスト表示 or グリッド表示
     */

    fun saveCollectionType(type: CollectionType) {
        preferences.edit().putString(KEY_COLLECTION_TYPE, type.name).apply()
    }

    fun loadCollectionType(): CollectionType {
        val string = preferences.getString(KEY_COLLECTION_TYPE, CollectionType.LIST.name)
        return CollectionType.valueOf(string!!)
    }

    companion object {
        const val KEY_APP_LAUNCH_COUNT = "AppLaunchCount"
        const val KEY_COLLECTION_TYPE = "CollectionType"
    }
}
