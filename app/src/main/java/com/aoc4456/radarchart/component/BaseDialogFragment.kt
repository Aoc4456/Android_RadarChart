package com.aoc4456.radarchart.component

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.screen.groupcreate.GroupCreateFragment

/**
 * Common Dialog Fragment to retain content even if life cycle changes
 *
 * 使い方
 * 1. 呼び出し側のFragment で、DialogListener を実装する
 * 2. BaseDialogFragment.newInstance() でオブジェクトを生成する
 * 3. dialogFragment.show()
 */
class BaseDialogFragment private constructor() : DialogFragment() {

    private val title get() = arguments?.getString(TITLE)
    private val message get() = arguments?.getString(MESSAGE)
    private val positiveText get() = arguments?.getString(POSITIVE_TEXT)
    private val negativeText get() = arguments?.getString(NEGATIVE_TEXT)

    lateinit var dialogListener: DialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = parentFragment as DialogListener
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

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        if (tag == GroupCreateFragment.TRASH_DIALOG_TAG) {
            (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.red_700))
        }
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
