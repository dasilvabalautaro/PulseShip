package com.globalhiddenodds.pulseship.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.databinding.FragmentProfileBinding
import com.globalhiddenodds.pulseship.datasource.database.Owner
import com.globalhiddenodds.pulseship.ui.extensions.toBitmap
import com.globalhiddenodds.pulseship.ui.platform.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun layoutId() = R.layout.fragment_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pathFilePhoto = ""
        ownerViewModel.retrieveOwner(ownerViewModel.uidCurrent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(
            inflater, container, false
        )
        //setHasOptionsMenu(true)

        observerViewModelOwner()
        observerViewModelResultUpdate()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPhoto.setOnClickListener {
            val action = ProfileFragmentDirections.nextActionCameraActivity()
            findNavController().navigate(action)
        }

        binding.btnGallery.setOnClickListener {
            val action = ProfileFragmentDirections.nextActionProfileToPermissionsStorage()
            findNavController().navigate(action)
        }

        binding.editTextName.setOnKeyListener { v, keyCode, _ ->
            enableKeyboard.handleKeyEvent(
                v,
                keyCode
            )
        }

        binding.btnSaveName.isEnabled = false
        binding.btnSaveName.setOnClickListener {
            ownerViewModel.updateOwner()
            notify(R.string.msg_sent_update)
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 5) {
                    ownerViewModel.updateNameOwner(s.toString())
                }
                binding.btnSaveName.isEnabled = s.length > 5
            }
        }

        binding.editTextName.addTextChangedListener(afterTextChangedListener)
    }

    override fun onStart() {
        super.onStart()
        if (pathFilePhoto.isNotEmpty()) {
            ownerViewModel.updatePhotoOwner(pathFilePhoto)
            binding.btnSaveName.isEnabled = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observerViewModelOwner() {
        ownerViewModel.ownerCurrent.observe(viewLifecycleOwner,
            Observer {
                it ?: return@Observer
                setDataOwner(it)
            }
        )
    }

    private fun observerViewModelResultUpdate() {
        ownerViewModel.taskResult.observe(viewLifecycleOwner,
            Observer { taskResult ->
                taskResult ?: return@Observer

                taskResult.error?.let {
                    showFailed(it)
                }
                taskResult.success?.let {
                    showSuccess(it)
                }
            }
        )
    }

    private fun showSuccess(message: String) {
        notify(message)
    }

    private fun showFailed(errorString: String) {
        notify(errorString)
    }

    private fun setDataOwner(owner: Owner) {
        binding.editTextName.setText(owner.name)
        if (owner.photoB64.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val bmp = owner.photoB64.toBitmap()
                activity!!.runOnUiThread {
                    binding.imagePhoto.setImageBitmap(bmp)
                }
            }
        }
    }
}