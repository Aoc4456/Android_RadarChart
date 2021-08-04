package com.aoc4456.radarchart.screen.chartcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.databinding.ChartCreateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChartCreateFragment : Fragment() {

    private lateinit var binding: ChartCreateFragmentBinding
    private val viewModel by viewModels<ChartCreateViewModel>()

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

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.colorView.setOnChooseColorListener(requireActivity()) { chooseColor ->
            Timber.d("選択カラー $chooseColor")
        }
    }
}
