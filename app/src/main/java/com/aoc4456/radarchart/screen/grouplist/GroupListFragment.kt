package com.aoc4456.radarchart.screen.grouplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aoc4456.radarchart.databinding.GroupListFragmentBinding
import com.aoc4456.radarchart.screen.chartcollection.ChartCollectionUtil
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupListFragment : Fragment() {

    private val viewModel by viewModels<GroupListViewModel>()
    private lateinit var binding: GroupListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GroupListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ツールバー
        binding.btnSetting.setOnClickListener {
            val action = GroupListFragmentDirections.actionGroupListFragmentToSettingFragment()
            findNavController().navigate(action)
        }

        // setup FAB
        binding.floatingActionButton.setOnClickListener {
            val action =
                GroupListFragmentDirections.actionGroupListFragmentToGroupCreateFragment(null)
            findNavController().navigate(action)
        }

        binding.testFloatingActionButton.setOnClickListener {
            val action = GroupListFragmentDirections.actionGroupListFragmentToTestFragment()
            findNavController().navigate(action)
        }

        // RecyclerView
        val count = ChartCollectionUtil.calcSpanCountBasedOnScreenSize(
            requireContext(),
            CollectionType.LIST
        )
        (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = count
        binding.recyclerView.adapter = GroupListAdapter(viewModel)

        // TODO BindingAdapter化する
        viewModel.groupList.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as GroupListAdapter).submitList(it)
        }

        viewModel.navigateToChartCollection.observe(viewLifecycleOwner) {
            val action =
                GroupListFragmentDirections.actionGroupListFragmentToChartCollectionFragment(it)
            findNavController().navigate(action)
        }

        viewModel.navigateToGroupEdit.observe(viewLifecycleOwner) {
            val action =
                GroupListFragmentDirections.actionGroupListFragmentToGroupCreateFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val groupItem = (binding.recyclerView.adapter as GroupListAdapter).longTappedItem
        if (groupItem != null) {
            viewModel.onSelectedContextMenu(groupItem, item.itemId)
        }
        return super.onContextItemSelected(item)
    }
}
