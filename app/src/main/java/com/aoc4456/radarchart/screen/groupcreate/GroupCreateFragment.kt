package com.aoc4456.radarchart.screen.groupcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.component.MultiEditTextOutput
import com.aoc4456.radarchart.databinding.GroupCreateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupCreateFragment : Fragment() {

    private lateinit var binding: GroupCreateFragmentBinding
    private val viewModel by viewModels<GroupCreateViewModel>()

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
        viewModel.onViewCreated()

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
    }
}
