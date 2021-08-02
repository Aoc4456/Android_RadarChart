package com.aoc4456.radarchart.component

import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * DialogFragmentを使用する場合は、このクラスを継承してください
 *
 * 使い方
 * 1. このクラスを継承したクラスは、コンストラクタを private に変更する
 * 2. コールバック用のインターフェースを作成し、プロパティで保持する。onAttach で リスナーを復元する
 * 3. 呼び出し側のFragment では、コールバック用のインターフェースを実装する
 */
open class BaseDialogFragment : DialogFragment() {

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
