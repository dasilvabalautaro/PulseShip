package com.globalhiddenodds.pulseship.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.globalhiddenodds.pulseship.ui.activities.MainActivity
import com.globalhiddenodds.pulseship.databinding.FragmentStartBinding

class StartFragment: Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        backgroundExecutor.schedule({
            val action = StartFragmentDirections.nextActionLoginDest()
            activity!!.runOnUiThread {
                findNavController().navigate(action)
            }
        }, 5, TimeUnit.SECONDS)

    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStartBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            val action = StartFragmentDirections.nextActionLoginDest()
            activity!!.runOnUiThread {
                findNavController().navigate(action)
            }
        }

        binding.btnClose.setOnClickListener {
            findNavController().backStack.clear()
            (activity as MainActivity).finish()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        (activity as MainActivity?)!!.setBottomNavigationVisibility(View.GONE)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}