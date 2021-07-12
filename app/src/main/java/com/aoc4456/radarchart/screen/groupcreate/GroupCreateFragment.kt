package com.aoc4456.radarchart.screen.groupcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aoc4456.radarchart.R

class GroupCreateFragment : Fragment() {

    private lateinit var viewModel: GroupCreateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_create_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(GroupCreateViewModel::class.java)

    }

    companion object {
        fun newInstance() = GroupCreateFragment()
    }
}
