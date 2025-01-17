package com.aoc4456.radarchart.screen.chartcollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.component.dialog.DialogType
import com.aoc4456.radarchart.component.dialog.ListDialogFragment
import com.aoc4456.radarchart.component.dialog.ListDialogListener
import com.aoc4456.radarchart.databinding.ChartCollectionFragmentBinding
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.datasource.database.OrderBy
import com.aoc4456.radarchart.util.calcSpanCountBasedOnScreenSize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartCollectionFragment : Fragment(), ListDialogListener {

    private val viewModel by viewModels<ChartCollectionViewModel>()
    private lateinit var binding: ChartCollectionFragmentBinding

    private val navArgs: ChartCollectionFragmentArgs by navArgs()

    private lateinit var fadeInAnimation: Animation
    private lateinit var fadeOutAnimation: Animation

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
        fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fadein)
        fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fadeout)

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
                fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        viewModel.onToggleButtonCheckedChanged(checkedId)
                        binding.recyclerView.startAnimation(fadeInAnimation)
                    }
                })
                binding.recyclerView.startAnimation(fadeOutAnimation)
            }
        }

        // ソートボタン
        binding.btnOrder.setOnClickListener {
            val listDialogFragment = ListDialogFragment.newInstance(
                type = DialogType.CHART_ORDER_BY,
                title = null,
                items = ChartCollectionUtil.getItemsForSortDialog(
                    labels = viewModel.groupData.value!!.labelList.map { it.text },
                    requireContext()
                )
            )
            listDialogFragment.show(childFragmentManager, "ORDER_BY_DIALOG_TAG")
        }

        /**
         * ViewModel Observer
         */
        viewModel.groupData.observe(viewLifecycleOwner) { group ->
            binding.btnOrder.text = ChartCollectionUtil.sortIndexToString(
                sortIndex = group.group.sortIndex,
                labels = group.labelList.map { it.text },
                context = requireContext()
            )
            binding.btnAscDesc.text =
                if (group.group.orderBy == OrderBy.ASC) getString(R.string.asc_order) else getString(R.string.desc_order)
        }

        viewModel.chartList.observe(viewLifecycleOwner) {
            submitList(it)
        }

        viewModel.listOrGrid.observe(viewLifecycleOwner) { type ->
            when (type) {
                CollectionType.LIST -> {
                    binding.recyclerView.adapter = ChartCollectionListAdapter(viewModel)
                    binding.toggleGroup.check(R.id.toggleButtonList)
                }
                else -> {
                    binding.recyclerView.adapter = ChartCollectionGridAdapter(viewModel)
                    binding.toggleGroup.check(R.id.toggleButtonGrid)
                }
            }
            val count = calcSpanCountBasedOnScreenSize(requireContext(), type)
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = count
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
        val indexedList = viewModel.getIndexedChartList(list)
        (binding.recyclerView.adapter as? ChartCollectionListAdapter)?.submitList(indexedList)
        (binding.recyclerView.adapter as? ChartCollectionGridAdapter)?.submitList(indexedList)
    }

    override fun onSelectListItemInDialog(dialogType: DialogType, index: Int) {
        viewModel.onSelectItemInSortDialog(index)
    }
}

enum class CollectionType {
    LIST,
    GRID
}
