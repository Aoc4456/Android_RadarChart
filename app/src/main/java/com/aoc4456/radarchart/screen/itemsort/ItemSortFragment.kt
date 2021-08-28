package com.aoc4456.radarchart.screen.itemsort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aoc4456.radarchart.databinding.ItemSortFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemSortFragment : Fragment() {

    private val viewModel by viewModels<ItemSortViewModel>()
    private lateinit var binding: ItemSortFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemSortFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }
}
