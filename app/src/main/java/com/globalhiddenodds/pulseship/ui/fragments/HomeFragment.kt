package com.globalhiddenodds.pulseship.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.globalhiddenodds.pulseship.ui.activities.MainActivity
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.databinding.FragmentHomeBinding
import com.globalhiddenodds.pulseship.ui.platform.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment() {
    private val args: HomeFragmentArgs by navArgs()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun layoutId() = R.layout.fragment_home

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(
            inflater, container, false)

        if(!ownerViewModel.stateLogin.value!!){
            observerViewModel()
            ownerViewModel.ownerExist()
        }
        //setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refreshMenu = args.refreshMenu

        if (refreshMenu == 1){
            (activity as AppCompatActivity?)!!.supportActionBar?.show()
            (activity as MainActivity?)!!.setBottomNavigationVisibility(View.VISIBLE)
        }

        binding.btnMessage.setOnClickListener {
            val action = HomeFragmentDirections.nextActionTracking()
            findNavController().navigate(action)
        }
    }

    /*override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(stateKeyUser, target)
        }
        super.onSaveInstanceState(outState)
    }*/

    private fun showLoginFailed(@StringRes errorString: Int){
        notify(errorString)

        val action = HomeFragmentDirections.nextActionFirstTime()
        findNavController().navigate(action)
    }

    private fun observerViewModel(){
        ownerViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer

                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    showLoginSuccess(it)
                }
            }
        )
    }

    private fun showLoginSuccess(message: String){
        ownerViewModel.setStateLogin(true)
        ownerViewModel.retrieveOwner(ownerViewModel.uidCurrent)
        notify(message)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}