package com.aoc4456.radarchart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aoc4456.radarchart.datasource.sharedpreferences.RadarChartPreferences
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: RadarChartPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // アプリ起動回数5回毎に、In App Review を要求
        val appLaunchCount = preferences.loadAppLaunchCount()
        Timber.d("アプリ起動回数 = $appLaunchCount")
        if (appLaunchCount >= 5) {
            preferences.resetAppLaunchCount()
            Timber.d("アプリ起動回数をリセット")
        }
    }
}
