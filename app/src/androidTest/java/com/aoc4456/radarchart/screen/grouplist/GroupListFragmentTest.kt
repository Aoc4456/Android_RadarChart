package com.aoc4456.radarchart.screen.grouplist

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.aoc4456.radarchart.MainActivity
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.ChartGroup
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GroupListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: RadarChartRepository

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun onCreateView() {
        // GIVEN
        val group = ChartGroup(title = "TITLE", color = -14654801, maximumValue = 100)
        val groupLabelTextList = listOf("Label1", "Label2", "Label3", "Label4", "label5")
        runBlocking {
            repository.saveGroup(group, groupLabelTextList, null)
        }

        // WHEN
        launchActivity()

        // THEN
        onView(withId(R.id.floatingActionButton)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar_sort_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnSetting)).check(matches(isDisplayed()))
    }

    private fun launchActivity(): ActivityScenario<MainActivity>? {
        return launch(MainActivity::class.java)
    }
}
