package com.aoc4456.radarchart.component.dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface BaseDialogListener {
    fun onClickButtonInDialog(dialogType: DialogType, buttonType: DialogButtonType)
}

interface ListDialogListener {
    fun onSelectListItemInDialog(dialogType: DialogType, index: Int)
}

@Parcelize
enum class DialogType : Parcelable {
    GROUP_DELETE,
    CHART_DELETE,
    CHART_ORDER_BY,
    ICON_IMAGE_SELECT
}

enum class DialogButtonType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL,
}
