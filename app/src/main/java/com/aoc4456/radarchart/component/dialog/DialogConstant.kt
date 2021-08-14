package com.aoc4456.radarchart.component.dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface DialogListener {
    fun onClickButtonInDialog(dialogType: DialogType, buttonType: DialogButtonType)
}

@Parcelize
enum class DialogType : Parcelable {
    GROUP_DELETE,
    CHART_DELETE
}

enum class DialogButtonType {
    POSITIVE,
    NEGATIVE
}
