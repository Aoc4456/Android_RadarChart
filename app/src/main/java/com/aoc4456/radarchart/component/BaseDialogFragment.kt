package com.aoc4456.radarchart.component

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
class BaseDialogFragment private constructor() : DialogFragment() {

    private val title = arguments?.getString(TITLE)
    private val message = arguments?.getString(MESSAGE)
    private val positiveText = arguments?.getString(POSITIVE_TEXT)
    private val negativeText = arguments?.getString(NEGATIVE_TEXT)

    lateinit var dialogListener: DialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                ("$context must implement DialogListener")
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
                    setPositiveButton(positiveText) { _, _ ->
                        dialogListener.onDialogPositiveClick(this@BaseDialogFragment)
                    }
                }
                negativeText?.let {
                    setNegativeButton(negativeText) { _, _ ->
                        dialogListener.onDialogNegativeClick(this@BaseDialogFragment)
                    }
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

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

interface DialogListener {
    fun onDialogPositiveClick(dialog: DialogFragment)
    fun onDialogNegativeClick(dialog: DialogFragment)
}
