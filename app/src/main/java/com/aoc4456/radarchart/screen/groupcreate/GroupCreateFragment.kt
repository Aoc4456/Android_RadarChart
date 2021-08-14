package com.aoc4456.radarchart.screen.groupcreate

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
import com.aoc4456.radarchart.component.MultiEditTextOutput
import com.aoc4456.radarchart.component.dialog.BaseDialogFragment
import com.aoc4456.radarchart.component.dialog.DialogButtonType
import com.aoc4456.radarchart.component.dialog.DialogListener
import com.aoc4456.radarchart.component.dialog.DialogType
import com.aoc4456.radarchart.databinding.GroupCreateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupCreateFragment : Fragment(), DialogListener {

    private lateinit var binding: GroupCreateFragmentBinding
    private val viewModel by viewModels<GroupCreateViewModel>()

    private val navArgs: GroupCreateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GroupCreateFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = this.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated(navArgs.groupWithLabelAndCharts)

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.titleEditText.doAfterTextChanged { editable ->
            viewModel.onChangeTitleText(editable.toString())
        }

        binding.colorView.setOnChooseColorListener(requireActivity()) { chooseColor ->
            viewModel.onChooseColor(chooseColor)
        }

        binding.numberOfItemsSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                viewModel.onSliderValueChanged(value)
            }
        }

        binding.maximumValueField.doAfterTextChanged { editable ->
            viewModel.onChangeMaximumText(editable.toString())
        }

        binding.multiEditText.setTextChangeListener(
            object : MultiEditTextOutput {
                override fun onMultiEditTextChanged(index: Int, text: String) {
                    viewModel.onTextChangeMultiEditText(index, text)
                }
            }
        )

        binding.toolbarTrashButton.setOnClickListener {
            val dialogFragment = BaseDialogFragment.newInstance(
                type = DialogType.GROUP_DELETE,
                title = getString(R.string.delete_group_title),
                message = getString(R.string.delete_group_message),
                positiveText = getString(R.string.delete),
                negativeText = getString(R.string.cancel)
            )
            dialogFragment.show(childFragmentManager, "TRASH_DIALOG_TAG")
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.dismiss.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    override fun onClickButtonInDialog(dialogType: DialogType, buttonType: DialogButtonType) {
        when (buttonType) {
            DialogButtonType.POSITIVE -> {
                viewModel.onClickTrashDialogPositive()
            }
            DialogButtonType.NEGATIVE -> {
            }
        }
    }
}
