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

    fun saveCollectionType(type: CollectionType) {
        preferences.edit().putString(KEY_COLLECTION_TYPE, type.name).apply()
    }

    fun loadCollectionType(): CollectionType {
        val string = preferences.getString(KEY_COLLECTION_TYPE, CollectionType.LIST.name)
        return CollectionType.valueOf(string!!)
    }

    companion object {
        const val KEY_COLLECTION_TYPE = "CollectionType"
    }
}
