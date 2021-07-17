package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupCreateViewModel : ViewModel() {

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    fun onSliderValueChanged(value: Float) {
        _numberOfItems.value = value.toInt()
    }

    fun onChooseColor(color: Int) {
        _groupColor.value = color
    }
}
