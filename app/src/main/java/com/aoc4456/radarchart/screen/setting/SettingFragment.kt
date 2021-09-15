package com.aoc4456.radarchart.screen.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.BuildConfig
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.databinding.SettingFragmentBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class SettingFragment : Fragment() {

    private lateinit var binding: SettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.privacyPolicy.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToPrivacyPolicyFragment()
            findNavController().navigate(action)
        }

        binding.license.setOnClickListener {
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
        }

        binding.reviewApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=com.aoc4456.radarchart")
            try {
                startActivity(intent)
            } catch (e: Exception) {
            }
        }

        binding.appVersionLabel.let {
            val versionText = it.resources.getString(
                R.string.app_version_with_value,
                BuildConfig.VERSION_NAME
            )
            it.text = versionText
        }
    }
}
