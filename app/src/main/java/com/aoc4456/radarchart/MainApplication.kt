package com.aoc4456.radarchart

import android.app.Application
import com.aoc4456.radarchart.datasource.sharedpreferences.RadarChartPreferences
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var preferences: RadarChartPreferences

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        preferences.incrementAppLaunchCount()
    }
}
