package com.aoc4456.radarchart.component

import android.os.Bundle
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment private constructor() : DialogFragment() {

    private val title = arguments?.getString(TITLE)
    private val message = arguments?.getString(MESSAGE)
    private val positiveText = arguments?.getString(POSITIVE_TEXT)
    private val negativeText = arguments?.getString(NEGATIVE_TEXT)

    companion object {
        fun newInstance(
            title: String,
            message: String?,
            positiveText: String,
            negativeText: String?
        ): BaseDialogFragment {
            val dialogFragment = BaseDialogFragment()

            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(MESSAGE, message)
            bundle.putString(POSITIVE_TEXT, positiveText)
            bundle.putString(NEGATIVE_TEXT, negativeText)

            dialogFragment.arguments = bundle
            return dialogFragment
        }

        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val POSITIVE_TEXT = "positiveText"
        private const val NEGATIVE_TEXT = "negativeText"
    }
}
