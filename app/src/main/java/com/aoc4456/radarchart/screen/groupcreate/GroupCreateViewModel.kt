package com.aoc4456.radarchart.screen.groupcreate

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.*
import com.aoc4456.radarchart.component.dialog.DialogButtonType
import com.aoc4456.radarchart.component.dialog.DialogType
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.util.ChartDataUtil
import com.aoc4456.radarchart.util.ImageUtil
import com.aoc4456.radarchart.util.PublishLiveData
import com.aoc4456.radarchart.util.ValidateInputFieldUtil.maximumValidate
import com.aoc4456.radarchart.util.ValidateInputFieldUtil.titleValidate
import com.aoc4456.radarchart.util.ValidateResult
import com.github.mikephil.charting.data.RadarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    application: Application,
    private val repository: RadarChartRepository
) : AndroidViewModel(application) {

    private val _groupArgs = MutableLiveData<GroupWithLabelAndCharts?>()
    val groupArgs: LiveData<GroupWithLabelAndCharts?> = _groupArgs

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private val _maximum = MutableLiveData("100")
    val maximum: LiveData<String> = _maximum

    private val _iconImage = MutableLiveData<Bitmap?>()
    val iconImage: LiveData<Bitmap?> = _iconImage

    private val iconImageByteArray: ByteArray?
        get() {
            if (iconImage.value == null) return null
            return ImageUtil.bitmapToByteArray(iconImage.value!!)
        }

    private var itemTextList =
        MutableLiveData(GroupCreateUtil.getDefaultTextList().toMutableList())

    val exactlySizedTextList = MediatorLiveData<List<String>>().apply {
        addSource(numberOfItems) {
            value = GroupCreateUtil.getExactlySizedTextList(itemTextList.value!!, it)
        }
        addSource(itemTextList) {
            value = GroupCreateUtil.getExactlySizedTextList(it, numberOfItems.value!!)
        }
    }

    val chartData = MediatorLiveData<RadarData>().apply {
        addSource(groupColor) {
            value = ChartDataUtil.getRadarDataWithTheSameValue(it, numberOfItems.value!!)
        }
        addSource(numberOfItems) {
            value = ChartDataUtil.getRadarDataWithTheSameValue(groupColor.value!!, it)
        }
    }

    val launchGallery = PublishLiveData<Boolean>()

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    fun onViewCreated(groupArgs: GroupWithLabelAndCharts?) {
        if (chartUpdate.value != null) return
        if (groupArgs != null) {
            _groupArgs.value = groupArgs
            _title.value = groupArgs.group.title
            _groupColor.value = groupArgs.group.color
            _maximum.value = groupArgs.group.maximumValue.toString()
            itemTextList.value = GroupCreateUtil.getMaximumSizeTextList(groupArgs.labelList)
            _numberOfItems.value = groupArgs.labelList.size
            groupArgs.group.iconImage?.let {
                _iconImage.value = ImageUtil.byteArrayToBitmap(it)
            }
        }
        updateChart()
    }

    fun onSliderValueChanged(newValue: Float) {
        if (_numberOfItems.value == newValue.toInt()) return
        _numberOfItems.value = newValue.toInt()
        updateChart()
    }

    fun onChooseColor(newColor: Int) {
        if (newColor == _groupColor.value) return
        _groupColor.value = newColor
        updateChart()
    }

    fun onClippedIconImage(uri: Uri) {
        val bitmap = ImageUtil.uriToBitmap(getApplication<Application>().contentResolver, uri)
        bitmap?.let { _iconImage.value = it }
    }

    fun onChangeTitleText(newText: String) {
        if (newText == title.value) return
        _title.value = newText
    }

    fun onChangeMaximumText(newMaximum: String) {
        if (newMaximum == _maximum.value) return
        _maximum.value = newMaximum
    }

    fun onTextChangeMultiEditText(index: Int, newText: String) {
        if (newText == itemTextList.value?.getOrNull(index)) return
        val newList = itemTextList.value!!
        newList[index] = newText
        itemTextList.value = newList
        updateChart()
    }

    fun onClickSaveButton() {
        // バリデーション
        val validateResult = validateInputField()
        val validateFail = !validateResult.first
        if (validateFail) {
            _errorMessage.value = validateResult.second!!
            return
        }

        // 保存
        val entity = createEntity()
        viewModelScope.launch {
            repository.saveGroup(entity.first, entity.second, groupArgs.value)
        }

        _dismiss.value = true
    }

    fun onClickButtonInDialog(dialogType: DialogType, buttonType: DialogButtonType) {
        when (dialogType) {
            // アイコン画像設定ダイアログ
            DialogType.ICON_IMAGE_SELECT -> {
                when (buttonType) {
                    // 画像を設定
                    DialogButtonType.POSITIVE -> {
                        launchGallery.value = true
                    }
                    else -> {
                        _iconImage.value = null
                    }
                }
            }

            // グループ削除ダイアログ
            DialogType.GROUP_DELETE -> {
                if (buttonType == DialogButtonType.POSITIVE) {
                    if (groupArgs.value == null) return
                    viewModelScope.launch {
                        repository.deleteGroup(groupArgs.value!!.group.id)
                    }
                    _dismiss.value = true
                }
            }
            else -> {
            }
        }
    }

    private fun updateChart() {
        _chartUpdate.value = true
    }

    private fun validateInputField(): Pair<Boolean, Int?> {
        val titleValidateResult = titleValidate(title.value!!)
        if (titleValidateResult != ValidateResult.SUCCESS) {
            return Pair(false, titleValidateResult.stringResId)
        }

        val maximumValidateResult = maximumValidate(maximum.value!!)
        if (maximumValidateResult != ValidateResult.SUCCESS) {
            return Pair(false, maximumValidateResult.stringResId)
        }

        return Pair(true, null)
    }

    private fun createEntity(): Pair<ChartGroup, List<String>> {
        val group = createChartGroup()
        val labels = exactlySizedTextList.value!!
        return Pair(group, labels)
    }

    private fun createChartGroup(): ChartGroup {
        val isNew = groupArgs.value == null

        if (isNew) {
            return ChartGroup(
                title = title.value!!,
                color = groupColor.value!!,
                maximumValue = maximum.value!!.toInt(),
                iconImage = iconImageByteArray
            )
        }

        val oldGroup = groupArgs.value!!.group
        return oldGroup.also {
            it.title = title.value!!
            it.color = groupColor.value!!
            it.maximumValue = maximum.value!!.toInt()
            it.iconImage = iconImageByteArray
            it.updatedAt = System.currentTimeMillis()
        }
    }
}
