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
import com.aoc4456.radarchart.component.dialog.BaseDialogFragment
import com.aoc4456.radarchart.component.dialog.BaseDialogListener
import com.aoc4456.radarchart.component.dialog.DialogButtonType
import com.aoc4456.radarchart.component.dialog.DialogType
import com.aoc4456.radarchart.databinding.GroupCreateFragmentBinding
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupCreateFragment : Fragment(), BaseDialogListener {

    private lateinit var binding: GroupCreateFragmentBinding
    private val viewModel by viewModels<GroupCreateViewModel>()

    private val navArgs: GroupCreateFragmentArgs by navArgs()

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            binding.iconView.setImageURI(uriContent)
        } else {
            // an error occurred
            val exception = result.error
        }
    }

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

        binding.multiEditText.setTextChangeListener { index, newText ->
            viewModel.onTextChangeMultiEditText(index, newText)
        }

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

        binding.iconView.setOnClickListener {
            cropImage.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setCropShape(CropImageView.CropShape.OVAL)
                    setFixAspectRatio(true)
                }
            )
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
