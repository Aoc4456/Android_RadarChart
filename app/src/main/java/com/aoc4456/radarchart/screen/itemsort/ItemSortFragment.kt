package com.aoc4456.radarchart.screen.itemsort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.aoc4456.radarchart.databinding.ItemSortFragmentBinding
import com.aoc4456.radarchart.util.ListItemTouchCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemSortFragment : Fragment() {

    private val viewModel by viewModels<ItemSortViewModel>()
    private lateinit var binding: ItemSortFragmentBinding

    private val navArgs: ItemSortFragmentArgs by navArgs()

    private val itemTouchHelper = ItemTouchHelper(ListItemTouchCallback())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemSortFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = this.viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated(navArgs)

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.recyclerView.adapter = ItemSortAdapter(viewModel, itemTouchHelper)
        (binding.recyclerView.adapter as ItemSortAdapter).notifyDataSetChanged()

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        viewModel.chartUpdate.observe(viewLifecycleOwner) {
            binding.radarChart.let { radarChart ->
                radarChart.data = viewModel.radarData
                radarChart.setChartItemLabel(viewModel.labelList.map { it.text })
                radarChart.notifyDataSetChanged()
                radarChart.invalidate()
            }
        }
    }
}
