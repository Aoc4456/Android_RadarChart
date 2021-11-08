package com.aoc4456.radarchart.screen.grouplist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aoc4456.radarchart.MainCoroutineRule
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.fake.FakeRepository
import com.aoc4456.radarchart.getOrAwaitValue
import com.aoc4456.radarchart.observeForTesting
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GroupListViewModelTest {

    // テスト対象クラス
    private lateinit var groupListViewModel: GroupListViewModel

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() = runBlocking {
        val fakeRepository = FakeRepository() // ViewModelに集中してテストするため偽のRepositoryを使う
        fakeRepository.setupDefaultData()
        groupListViewModel = GroupListViewModel(fakeRepository)
    }

    @Test
    fun onViewCreatedAndGetGroupList() {
        // WHEN
        groupListViewModel.onViewCreated()

        // THEN
        groupListViewModel.groupList.observeForTesting {
            assertThat(groupListViewModel.groupList.getOrAwaitValue().size, `is`(2))
        }
    }

    @Test
    fun onClickListItemAndCallNavigateEvent() {
        // GIVEN
        val clickedItem = mockk<GroupWithLabelAndCharts>()

        // WHEN
        groupListViewModel.onClickListItem(clickedItem)

        // THEN
        groupListViewModel.navigateToChartCollection.observeForTesting {
            assertThat(
                groupListViewModel.navigateToChartCollection.getOrAwaitValue(),
                `is`(notNullValue())
            )
        }
    }

    @Test
    fun onSelectedContextMenu() {
        // GIVEN
        val groupItem = mockk<GroupWithLabelAndCharts>()
    }
}
