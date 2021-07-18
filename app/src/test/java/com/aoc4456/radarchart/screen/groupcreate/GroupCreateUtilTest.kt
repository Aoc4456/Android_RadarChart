package com.aoc4456.radarchart.screen.groupcreate

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class GroupCreateUtilTest {

    @Test
    fun getText7_Size5_Return5() {
        // Given
        val textList = listOf<String>("1", "2", "3", "4", "5", "6", "7")
        val size = 5

        // When
        val result = GroupCreateUtil.getExactlySizedTextList(textList, size)

        // Then
        assertThat(result.size, `is`(5))
    }

    @Test
    fun getText7_Size5_Return5_From_Beginning() {
        // Given
        val textList = listOf<String>("0", "1", "2", "3", "4", "5", "6")
        val size = 5

        // When
        val result = GroupCreateUtil.getExactlySizedTextList(textList, size)

        // Then
        for (i in result.indices) {
            assertThat(result[i], `is`(i.toString()))
        }
    }

    @Test
    fun getText5_Size7_Return5() {
        // Given
        val textList = listOf<String>("1", "2", "3", "4", "5")
        val size = 7

        // When
        val result = GroupCreateUtil.getExactlySizedTextList(textList, size)

        // Then
        assertThat(result.size, `is`(5))
    }
}
