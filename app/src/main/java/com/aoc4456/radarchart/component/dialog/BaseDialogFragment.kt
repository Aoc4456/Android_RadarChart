package com.aoc4456.radarchart.component.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * Common Dialog Fragment to retain content even if life cycle changes
 *
 * 使い方
 * 1. 呼び出し側のFragment で、DialogListener を実装する
 * 2. BaseDialogFragment.newInstance() でオブジェクトを生成する
 * 3. dialogFragment.show()
 */
class BaseDialogFragment : DialogFragment() {

    private val dialogType get() = arguments?.getParcelable<DialogType>(DIALOG_TYPE)
    private val title get() = arguments?.getString(TITLE)
    private val message get() = arguments?.getString(MESSAGE)
    private val positiveText get() = arguments?.getString(POSITIVE_TEXT)
    private val negativeText get() = arguments?.getString(NEGATIVE_TEXT)
    private val neutralText get() = arguments?.getString(NEUTRAL_TEXT)

    private lateinit var dialogListener: BaseDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = parentFragment as BaseDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                ("$context must implement BaseDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            builder.run {
                title?.let { setTitle(title) }
                message?.let { setMessage(message) }
                positiveText?.let {
                    setPositiveButton(it) { _, _ ->
                        dialogListener.onClickButtonInDialog(
                            dialogType!!,
                            DialogButtonType.POSITIVE
                        )
                    }
                }
                negativeText?.let {
                    setNegativeButton(it) { _, _ ->
                        dialogListener.onClickButtonInDialog(
                            dialogType!!,
                            DialogButtonType.NEGATIVE
                        )
                    }
                }
                neutralText?.let {
                    setNeutralButton(it) { _, _ ->
                        dialogListener.onClickButtonInDialog(dialogType!!, DialogButtonType.NEUTRAL)
                    }
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { setButtonTextColor(it as AlertDialog) }
    }

    private fun setButtonTextColor(dialog: AlertDialog) {
        if (dialogType in listOf(DialogType.GROUP_DELETE, DialogType.CHART_DELETE)) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }
    }

    companion object {
        fun newInstance(
            type: DialogType,
            title: String,
            message: String? = null,
            positiveText: String,
            negativeText: String? = null,
            neutralText: String? = null
        ): BaseDialogFragment {
            val dialogFragment = BaseDialogFragment()

            val bundle = Bundle()

            bundle.putParcelable(DIALOG_TYPE, type)
            bundle.putString(TITLE, title)
            bundle.putString(MESSAGE, message)
            bundle.putString(POSITIVE_TEXT, positiveText)
            bundle.putString(NEGATIVE_TEXT, negativeText)
            bundle.putString(NEUTRAL_TEXT, neutralText)

            dialogFragment.arguments = bundle
            return dialogFragment
        }

        private const val DIALOG_TYPE = "dialogType"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val POSITIVE_TEXT = "positiveText"
        private const val NEGATIVE_TEXT = "negativeText"
        private const val NEUTRAL_TEXT = "neutralText"
    }
}
