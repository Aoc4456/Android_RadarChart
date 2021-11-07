package com.aoc4456.radarchart.screen.grouplist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aoc4456.radarchart.MainCoroutineRule
import com.aoc4456.radarchart.datasource.fake.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun setupViewModel() {
        val fakeRepository = FakeRepository() // ViewModelに集中してテストするため偽のRepositoryを使う
        groupListViewModel = GroupListViewModel(fakeRepository)
    }

    @Test
    fun onViewCreatedAndGetGroupList() {
        // GIVEN
        groupListViewModel.onViewCreated()
    }

    @Test
    fun onClickListItem() {
    }

    @Test
    fun onSelectedContextMenu() {
    }
}
