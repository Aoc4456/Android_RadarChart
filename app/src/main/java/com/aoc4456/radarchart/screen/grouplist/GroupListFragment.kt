package com.aoc4456.radarchart.screen.grouplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.databinding.GroupListFragmentBinding

class GroupListFragment : Fragment() {

    private lateinit var viewModel: GroupListViewModel
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
        viewModel = ViewModelProvider(this).get(GroupListViewModel::class.java)

        // setup FAB
        binding.floatingActionButton.setOnClickListener {
            val action = GroupListFragmentDirections.actionGroupListFragmentToGroupCreateFragment()
            findNavController().navigate(action)
        }

        // TODO: RecyclerViewにAdapterを設定
        // TODO: viewModelの
    }
}
