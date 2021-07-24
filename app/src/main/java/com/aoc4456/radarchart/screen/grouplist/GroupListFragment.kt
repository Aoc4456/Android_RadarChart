package com.aoc4456.radarchart.screen.grouplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.databinding.GroupListFragmentBinding
import com.aoc4456.radarchart.screen.groupcreate.GroupCreateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupListFragment : Fragment() {

    private val viewModel by viewModels<GroupCreateViewModel>()
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
        viewModel.onViewCreated()

        // setup FAB
        binding.floatingActionButton.setOnClickListener {
            val action = GroupListFragmentDirections.actionGroupListFragmentToGroupCreateFragment()
            findNavController().navigate(action)
        }

        binding.testFloatingActionButton.setOnClickListener {
            val action = GroupListFragmentDirections.actionGroupListFragmentToTestFragment()
            findNavController().navigate(action)
        }

        // TODO: RecyclerViewにAdapterを設定
        // TODO: viewModelの
    }
}
