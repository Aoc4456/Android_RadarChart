package com.aoc4456.radarchart.screen.grouplist

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.aoc4456.radarchart.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GroupListFragmentTest {

//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//    @Before
//    fun init() {
//        // Populate @Inject fields in test class
//        hiltRule.inject()
//    }

    @Test
    fun onCreateView() {
    }

    @Test
    fun onViewCreated() {
    }

    @Test
    fun onContextItemSelected() {
    }

    private fun launchActivity(): ActivityScenario<MainActivity>? {
        return launch(MainActivity::class.java)
    }
}
