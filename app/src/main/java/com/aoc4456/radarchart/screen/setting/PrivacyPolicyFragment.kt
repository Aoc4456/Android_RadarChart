package com.aoc4456.radarchart.screen.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.databinding.PrivacyPolicyFragmentBinding

class PrivacyPolicyFragment : Fragment() {

    private lateinit var binding: PrivacyPolicyFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PrivacyPolicyFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.webviewPrivacyPolicy.loadUrl("https://radarchart-aocm.web.app/")
    }
}
