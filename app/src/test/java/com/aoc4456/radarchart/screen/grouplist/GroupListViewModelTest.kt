package com.aoc4456.radarchart.screen.grouplist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aoc4456.radarchart.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GroupListViewModelTest {

    private lateinit var groupListViewModel: GroupListViewModel

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // TODO テスト用のFakeRepositoryが必要

    @Test
    fun onViewCreated() {
    }

    @Test
    fun onClickListItem() {
    }

    @Test
    fun onSelectedContextMenu() {
    }
}
