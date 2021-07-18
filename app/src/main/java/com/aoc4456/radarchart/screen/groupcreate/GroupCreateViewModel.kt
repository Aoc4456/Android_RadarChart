package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupCreateViewModel : ViewModel() {

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private val itemTextList = listOf("項目1", "項目2", "項目3", "項目4", "項目5", "項目6", "項目7", "項目8")

    private val _exactlySizedTextList = MutableLiveData<List<String>>()
    val exactlySizedTextList: LiveData<List<String>> = _exactlySizedTextList

    fun onViewCreated() {
        _exactlySizedTextList.value =
            GroupCreateUtil.getExactlySizedTextList(itemTextList, numberOfItems.value!!)
    }

    fun onSliderValueChanged(value: Float) {
        _numberOfItems.value = value.toInt()
        _exactlySizedTextList.value =
            GroupCreateUtil.getExactlySizedTextList(itemTextList, value.toInt())
    }

    fun onChooseColor(color: Int) {
        _groupColor.value = color
    }
}
