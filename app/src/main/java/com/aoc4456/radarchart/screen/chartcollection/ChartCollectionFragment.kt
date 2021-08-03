package com.aoc4456.radarchart.screen.chartcollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.aoc4456.radarchart.databinding.ChartCollectionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartCollectionFragment : Fragment() {

    private val viewModel by viewModels<ChartCollectionViewModel>()
    private lateinit var binding: ChartCollectionFragmentBinding

    private val navArgs: ChartCollectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChartCollectionFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = this.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navArgsText.text = navArgs.groupWithLabelAndCharts!!.group.title
    }
}
