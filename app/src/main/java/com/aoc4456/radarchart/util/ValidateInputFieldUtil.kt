package com.aoc4456.radarchart.util

import com.aoc4456.radarchart.R

enum class ValidateResult(val stringResId: Int?) {
    SUCCESS(null),
    TITLE_EMPTY(R.string.title_is_empty),
    MAXIMUM_EMPTY(R.string.maximum_is_empty),
    MAXIMUM_ILLEGAL(R.string.maximum_is_illegal)
}

object ValidateInputFieldUtil { // TODO テストを書く

    fun titleValidate(title: String): ValidateResult {
        if (title.isEmpty()) return ValidateResult.TITLE_EMPTY
        return ValidateResult.SUCCESS
    }

    fun maximumValidate(maximum: String): ValidateResult {
        if (maximum.isEmpty()) return ValidateResult.MAXIMUM_EMPTY
        try {
            maximum.toInt()
        } catch (e: ClassCastException) {
            return ValidateResult.MAXIMUM_ILLEGAL
        }
        return ValidateResult.SUCCESS
    }
}
