package com.aoc4456.radarchart.datasource.database

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
    @Transaction
    suspend fun getGroupListWithDetail(): List<GroupWithLabelAndCharts> {
        val list = mutableListOf<GroupWithLabelAndCharts>()
        val idList = getGroupIdList()
        idList.forEach {
            list.add(getGroupWithLabelAndCharts(it))
        }
        return list
    }

    @Query("SELECT id FROM ChartGroup ORDER BY rate ASC")
    suspend fun getGroupIdList(): List<String>

    @Transaction
    suspend fun getGroupWithLabelAndCharts(groupId: String): GroupWithLabelAndCharts {
        val group = getGroupById(groupId)
        val label = getGroupLabel(groupId)
        val charts = getChartList(groupId)
        return GroupWithLabelAndCharts(
            group = group,
            labelList = label,
            chartList = charts
        )
    }

    @Query("SELECT * From ChartGroup WHERE id = :groupId")
    suspend fun getGroupById(groupId: String): ChartGroup

    @Query("SELECT * From ChartGroupLabel WHERE chartGroupId = :groupId ORDER BY `index` ASC")
    suspend fun getGroupLabel(groupId: String): List<ChartGroupLabel>

    @Query("SELECT * FROM MyChart WHERE chartGroupId = :groupId ORDER BY createdAt ASC")
    suspend fun getChartList(groupId: String): List<MyChart>

    @Query("SELECT * FROM ChartValue WHERE myChartId = :chartId ORDER BY `index` ASC")
    suspend fun getChartValueById(chartId: String): List<ChartValue>

    @Query("SELECT MAX(rate) FROM ChartGroup")
    suspend fun getMaxRate(): Int?

    /**
     * Update
     */
    @Update
    suspend fun updateGroup(group: ChartGroup)

    @Update
    suspend fun updateGroupLabel(label: ChartGroupLabel)

    @Update
    suspend fun updateChartValue(value: ChartValue)

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
            val startIndex = oldGroup.labelList.size
            val last = startIndex + numberOfItemsDiff

            oldGroup.chartList.forEach { chart ->
                for (i in startIndex until last) {
                    insertChartValue(
                        ChartValue(
                            myChartId = chart.id,
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
                deleteChartValueGreaterThanIndex(chart.id, labels.size)
            }
            if (0 <= group.sortIndex) {
                resetSortIndex(group.id)
            }
        }
    }

    @Transaction
    suspend fun setRates(list: List<ChartGroup>) {
        for (i in list.indices) {
            setRate(
                groupId = list[i].id,
                rate = i
            )
        }
    }

    @Query("UPDATE ChartGroup SET orderBy = :orderBy WHERE id = :groupId")
    suspend fun changeAscDesc(groupId: String, orderBy: OrderBy)

    @Query("UPDATE ChartGroup SET sortIndex = :sortIndex WHERE id = :groupId")
    suspend fun updateSortIndex(groupId: String, sortIndex: Int)

    @Query("UPDATE ChartGroup SET sortIndex = -1 WHERE id = :groupId")
    suspend fun resetSortIndex(groupId: String)

    @Query("UPDATE ChartGroup SET rate = :rate WHERE id = :groupId")
    suspend fun setRate(groupId: String, rate: Int)

    @Transaction
    suspend fun swapGroupLabel(group: GroupWithLabelAndCharts, newList: List<ChartGroupLabel>) {
        val oldList = group.labelList
        for (i in oldList.indices) {
            val oldLabel = oldList[i]
            val movedLabel = newList.find { it.id == oldLabel.id }
            val movePosition = newList.indexOf(movedLabel)

            val oldIndex = oldLabel.index

            if (oldLabel.index != movePosition) {
                updateGroupLabel(oldLabel.apply { this.index = movePosition })
                group.chartList.forEach {
                    swapChartValueIndex(it.id, oldIndex, movePosition)
                }
            }
        }
        resetSortingState()
    }

    @Query("UPDATE ChartValue SET 'index' = :newIndex,sortingState = 1 WHERE myChartId = :chartId AND `index` = :oldIndex AND sortingState = 0")
    suspend fun swapChartValueIndex(chartId: String, oldIndex: Int, newIndex: Int)

    @Query("UPDATE ChartValue SET sortingState = 0")
    suspend fun resetSortingState()

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
