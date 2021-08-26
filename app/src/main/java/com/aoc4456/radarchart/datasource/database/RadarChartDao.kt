package com.aoc4456.radarchart.datasource.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RadarChartDao {

    /**
     * Create Group
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: ChartGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupLabel(label: ChartGroupLabel)

    @Transaction
    suspend fun saveGroupAndLabel(group: ChartGroup, labels: List<ChartGroupLabel>) {
        insertGroup(group)
        labels.forEach {
            insertGroupLabel(it)
        }
    }

    /**
     * Create Chart
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChart(chart: MyChart)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChartValue(chartValue: ChartValue)

    @Transaction
    suspend fun saveChartAndValues(chart: MyChart, values: List<ChartValue>) {
        insertChart(chart)
        values.forEach {
            insertChartValue(it)
        }
    }

    /**
     * Read
     */
    // TODO 並び順の管理
    @Transaction
    @Query("SELECT * FROM ChartGroup ORDER BY rate ASC")
    fun observeGroupWithLabelAndCharts(): LiveData<List<GroupWithLabelAndCharts>>

    @Transaction
    @Query("SELECT * From ChartGroup WHERE id = :groupId")
    suspend fun getGroupById(groupId: String): GroupWithLabelAndCharts

    @Transaction
    @Query("SELECT * FROM MyChart WHERE chartGroupId = :groupId")
    suspend fun getChartList(groupId: String): List<MyChartWithValue>

    @Query("SELECT MAX(rate) FROM ChartGroup")
    suspend fun getMaxRate(): Int?

    /**
     * Update
     */
    @Update
    suspend fun updateGroup(group: ChartGroup)

    @Transaction
    suspend fun updateGroupAndLabel(
        group: ChartGroup,
        labels: List<ChartGroupLabel>,
        oldGroup: GroupWithLabelAndCharts
    ) {
        updateGroup(group)

        deleteGroupLabel(group.id)
        labels.forEach {
            insertGroupLabel(it)
        }

        val numberOfItemsDiff = labels.size - oldGroup.labelList.size
        // 項目数増加時、グループに属するチャートのValueをデフォルト値で増やす
        if (numberOfItemsDiff > 0) {
            val startIndex = labels.size
            val last = startIndex + numberOfItemsDiff

            oldGroup.chartList.forEach { chart ->
                for (i in startIndex until last) {
                    insertChartValue(
                        ChartValue(
                            myChartId = chart.myChart.id,
                            index = i,
                            value = group.maximumValue * 0.6
                        )
                    )
                }
            }
        }

        // 項目数減少時、グループに属するチャートの値の数を減らす
        // また、ソート条件が項目名の場合、条件を作成日にリセットする
        if (numberOfItemsDiff < 0) {
            oldGroup.chartList.forEach { chart ->
                deleteChartValueGreaterThanIndex(chart.myChart.id, labels.size)
            }
            if (0 <= group.sortIndex) {
                resetSortIndex(group.id)
            }
        }
    }

    @Query("UPDATE ChartGroup SET orderBy = :orderBy WHERE id = :groupId")
    suspend fun changeAscDesc(groupId: String, orderBy: OrderBy)

    @Query("UPDATE ChartGroup SET sortIndex = :sortIndex WHERE id = :groupId")
    suspend fun updateSortIndex(groupId: String, sortIndex: Int)

    @Query("UPDATE ChartGroup SET sortIndex = -1 WHERE id = :groupId")
    suspend fun resetSortIndex(groupId: String)

    /**
     * Delete
     */

    @Query("DELETE FROM ChartGroup WHERE id = :groupId")
    suspend fun deleteGroup(groupId: String)

    @Query("DELETE FROM ChartGroupLabel WHERE chartGroupId = :groupId")
    suspend fun deleteGroupLabel(groupId: String)

    @Query("DELETE FROM MyChart WHERE id = :chartId")
    suspend fun deleteMyChart(chartId: String)

    @Query("DELETE FROM ChartValue WHERE myChartId = :chartId AND `index` >= :index")
    suspend fun deleteChartValueGreaterThanIndex(chartId: String, index: Int)
}
