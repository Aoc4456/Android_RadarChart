package com.aoc4456.radarchart.screen.chartcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.component.dialog.BaseDialogFragment
import com.aoc4456.radarchart.component.dialog.BaseDialogListener
import com.aoc4456.radarchart.component.dialog.DialogButtonType
import com.aoc4456.radarchart.component.dialog.DialogType
import com.aoc4456.radarchart.databinding.ChartCreateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartCreateFragment : Fragment(), BaseDialogListener {

    private lateinit var binding: ChartCreateFragmentBinding
    private val viewModel by viewModels<ChartCreateViewModel>()

    private val navArgs: ChartCreateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChartCreateFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = this.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onViewCreated(navArgs)

        // ツールバー
        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbarTrashButton.setOnClickListener {
            val dialogFragment = BaseDialogFragment.newInstance(
                type = DialogType.CHART_DELETE,
                title = getString(R.string.delete_chart_title),
                message = getString(R.string.delete_chart_message),
                positiveText = getString(R.string.delete),
                negativeText = getString(R.string.cancel)
            )
            dialogFragment.show(childFragmentManager, "TRASH_DIALOG_TAG")
        }

        // タイトル
        binding.titleEditText.doAfterTextChanged { editable ->
            viewModel.onChangeTitleText(editable.toString())
        }

        // 色選択
        binding.colorView.setOnChooseColorListener(requireActivity()) { chooseColor ->
            viewModel.onChooseColor(chooseColor)
        }

        // チャートの値入力
        binding.multiInputView.setup(
            labels = viewModel.chartLabels.value!!,
            initialValues = viewModel.chartIntValues.value!!,
            maximum = viewModel.chartMaximum.value!! * 2,
            onChangeValueCallback = { index, newValue ->
                viewModel.onChangeChartIntValue(index, newValue)
            }
        )

        // コメントTextView
        binding.noteEditText.doAfterTextChanged { editable ->
            viewModel.onChangeComment(editable.toString())
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.dismiss.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    override fun onClickButtonInDialog(dialogType: DialogType, buttonType: DialogButtonType) {
        viewModel.onClickButtonInDialog(dialogType, buttonType)
    }
}
