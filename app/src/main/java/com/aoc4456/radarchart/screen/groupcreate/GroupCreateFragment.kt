package com.aoc4456.radarchart.screen.groupcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.databinding.GroupCreateFragmentBinding

class GroupCreateFragment : Fragment() {

    private lateinit var binding: GroupCreateFragmentBinding
    private lateinit var viewModel: GroupCreateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GroupCreateFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(GroupCreateViewModel::class.java)

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
