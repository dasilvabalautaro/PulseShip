package com.globalhiddenodds.pulseship.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.globalhiddenodds.pulseship.databinding.FragmentSettingsBinding

class SettingFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(
            inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}