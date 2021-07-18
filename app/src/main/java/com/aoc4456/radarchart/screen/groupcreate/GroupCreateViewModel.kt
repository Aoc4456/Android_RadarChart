package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupCreateViewModel : ViewModel() {

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private val itemTextList = listOf<String>()

    private val _exactlySizedTextList = MutableLiveData<List<String>>()
    val exactlySizedTextList: LiveData<List<String>> = _exactlySizedTextList

    fun onSliderValueChanged(value: Float) {
        _numberOfItems.value = value.toInt()
        _exactlySizedTextList.value =
            GroupCreateUtil.getExactlySizedTextList(itemTextList, value.toInt())
    }

    fun onChooseColor(color: Int) {
        _groupColor.value = color
    }
}
