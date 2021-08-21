package com.aoc4456.radarchart.component.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ListDialogFragment : DialogFragment() {

    private val dialogType get() = arguments?.getParcelable<DialogType>(DIALOG_TYPE)
    private val title get() = arguments?.getString(TITLE)
    private val items get() = arguments?.getStringArray(ITEMS)

    private lateinit var dialogListener: ListDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = parentFragment as ListDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                ("$context must implement ListDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.run {
                title?.let { setTitle(it) }
                setItems(items) { _, which ->
                    dialogListener.onSelectListItemInDialog(dialogType!!, which)
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun newInstance(
            type: DialogType,
            title: String?,
            items: List<String>,
        ): ListDialogFragment {
            val dialogFragment = ListDialogFragment()

            val bundle = Bundle()

            bundle.putParcelable(DIALOG_TYPE, type)
            bundle.putString(TITLE, title)
            @Suppress("UNCHECKED_CAST")
            bundle.putStringArray(ITEMS, items.toTypedArray())

            dialogFragment.arguments = bundle
            return dialogFragment
        }

        private const val DIALOG_TYPE = "dialogType"
        private const val TITLE = "title"
        private const val ITEMS = "items"
    }
}
