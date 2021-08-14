package com.aoc4456.radarchart.screen.chartcollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.aoc4456.radarchart.R
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
        viewModel.onViewCreated(navArgs)

        binding.toolbarBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.floatingActionButton.setOnClickListener {
            val action =
                ChartCollectionFragmentDirections.actionChartCollectionFragmentToChartCreateFragment(
                    navArgs.groupWithLabelAndCharts!!,
                    null
                )
            findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = ChartCollectionListAdapter(viewModel)

        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.toggleButtonList -> {
                        (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 1
                        binding.recyclerView.adapter = ChartCollectionListAdapter(viewModel)
                    }
                    R.id.toggleButtonGrid -> {
                        (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 3
                        binding.recyclerView.adapter = ChartCollectionGridAdapter(viewModel)
                    }
                }
            }
        }

        viewModel.chartList.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as? ChartCollectionListAdapter)?.submitList(it)
            (binding.recyclerView.adapter as? ChartCollectionGridAdapter)?.submitList(it)
        }

        viewModel.navigateToChartEdit.observe(viewLifecycleOwner) {
            val action =
                ChartCollectionFragmentDirections.actionChartCollectionFragmentToChartEditFragment(
                    navArgs.groupWithLabelAndCharts!!,
                    it
                )
            findNavController().navigate(action)
        }
    }
}
