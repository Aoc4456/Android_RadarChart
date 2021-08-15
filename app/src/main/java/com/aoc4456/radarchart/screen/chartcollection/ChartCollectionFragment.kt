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
import com.aoc4456.radarchart.databinding.ChartCollectionFragmentBinding
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
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

        // ツールバー
        binding.toolbarBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // FAB
        binding.floatingActionButton.setOnClickListener {
            val action =
                ChartCollectionFragmentDirections.actionChartCollectionFragmentToChartCreateFragment(
                    navArgs.groupWithLabelAndCharts!!,
                    null
                )
            findNavController().navigate(action)
        }

        // トグルボタン
        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                viewModel.onToggleButtonCheckedChanged(checkedId)
            }
        }

        /**
         * ViewModel Observer
         */

        viewModel.chartList.observe(viewLifecycleOwner) {
            submitList(it)
        }

        viewModel.listOrGrid.observe(viewLifecycleOwner) { type ->
            when (type) {
                CollectionType.LIST -> {
                    binding.recyclerView.adapter = ChartCollectionListAdapter(viewModel)
                    (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 1
                }
                else -> {
                    binding.recyclerView.adapter = ChartCollectionGridAdapter(viewModel)
                    (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 3
                }
            }
            viewModel.chartList.value?.let { submitList(it) }
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

    private fun submitList(list: List<MyChartWithValue>) {
        (binding.recyclerView.adapter as? ChartCollectionListAdapter)?.submitList(list)
        (binding.recyclerView.adapter as? ChartCollectionGridAdapter)?.submitList(list)
    }
}

enum class CollectionType {
    LIST,
    GRID
}
