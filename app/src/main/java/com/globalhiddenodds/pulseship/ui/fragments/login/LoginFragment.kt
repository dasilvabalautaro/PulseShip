package com.globalhiddenodds.pulseship.ui.fragments.login

import androidx.annotation.StringRes
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.databinding.FragmentLoginBinding
import com.globalhiddenodds.pulseship.ui.platform.BaseFragment
import com.globalhiddenodds.pulseship.ui.util.EnableKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_login
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(
            inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextLogin.setOnKeyListener {
                v, keyCode, _ -> enableKeyboard.handleKeyEvent(v, keyCode) }
        binding.editTextPassword.setOnKeyListener {
                v, keyCode, _ -> enableKeyboard.handleKeyEvent(v, keyCode) }
        binding.btnQuery.isEnabled = false
        binding.btnSignUp.isEnabled = false
        binding.btnQuery.setOnClickListener {
            binding.loading.visibility = View.VISIBLE

            ownerViewModel.signIn(
                binding.editTextLogin.text.toString(),
                binding.editTextPassword.text.toString()
            )

        }
        binding.btnSignUp.setOnClickListener {
            ownerViewModel.createOwner(
                binding.editTextLogin.text.toString(),
                binding.editTextPassword.text.toString())
            //,
            //                activity as AppCompatActivity
        }

        ownerViewModel.inputLoginLiveData.observe(viewLifecycleOwner,
            Observer { inputLoginState ->
                if (inputLoginState == null){
                    return@Observer
                }
                binding.btnQuery.isEnabled = inputLoginState.isDataValid
                binding.btnSignUp.isEnabled = inputLoginState.isDataValid
                inputLoginState.emailError?.let {
                    binding.editTextLogin.error = getString(it)
                }
                inputLoginState.passwordError?.let {
                    binding.editTextPassword.error = getString(it)
                }
            }
        )

        ownerViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                binding.loading.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            }
        )


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                ownerViewModel.verifyDataEntry(
                    binding.editTextLogin.text.toString(),
                    binding.editTextPassword.text.toString()
                )
            }
        }

        binding.editTextLogin.addTextChangedListener(afterTextChangedListener)
        binding.editTextPassword.addTextChangedListener(afterTextChangedListener)
        binding.editTextPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE &&
                binding.editTextLogin.error.isNullOrEmpty() &&
                binding.editTextPassword.error.isNullOrEmpty()) {
                binding.btnQuery.performClick()
            }
            false
        }

    }

    private fun updateUiWithUser(nameOwner: String) {
        ownerViewModel.setStateLogin(true)

        val welcome = getString(R.string.welcome) + nameOwner
        notify(welcome)
        val refreshMenu = 1
        val action = LoginFragmentDirections.actionGlobalHomeDest(refreshMenu)
        findNavController().navigate(action)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        notify(errorString)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}