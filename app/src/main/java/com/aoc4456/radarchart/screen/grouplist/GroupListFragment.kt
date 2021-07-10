package com.aoc4456.radarchart.screen.grouplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aoc4456.radarchart.R

class GroupListFragment : Fragment() {

    private lateinit var viewModel: GroupListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(GroupListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance() = GroupListFragment()
    }
}
