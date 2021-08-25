package com.aoc4456.radarchart.screen.grouplistsort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.databinding.GroupSortFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupSortFragment : Fragment() {

    private lateinit var binding: GroupSortFragmentBinding
    private val viewModel by viewModels<GroupSortViewModel>()

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

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
