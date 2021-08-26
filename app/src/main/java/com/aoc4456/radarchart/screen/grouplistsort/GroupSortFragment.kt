package com.aoc4456.radarchart.screen.grouplistsort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.aoc4456.radarchart.databinding.GroupSortFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GroupSortFragment : Fragment() {

    private lateinit var binding: GroupSortFragmentBinding
    private val viewModel by viewModels<GroupSortViewModel>()

    private val navArgs: GroupSortFragmentArgs by navArgs()

    private val itemTouchHelper = ItemTouchHelper(GroupItemTouchCallback())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GroupSortFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onViewCreated(navArgs)

        Timber.d("並び替え画面の引数 ${navArgs.grouplist.size}")

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.recyclerview.adapter = GroupSortAdapter(viewModel)
        (binding.recyclerview.adapter as GroupSortAdapter).notifyDataSetChanged()

        itemTouchHelper.attachToRecyclerView(binding.recyclerview)
    }
}
