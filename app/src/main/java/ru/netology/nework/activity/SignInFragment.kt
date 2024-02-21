package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentSignInBinding
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.viewmodel.SignInViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.login)
        toolbar.menu.clear()

        val binding = FragmentSignInBinding.inflate(layoutInflater)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        binding.signInButton.setOnClickListener {
            var errorFlag = false
            with(binding) {
                if (loginField.text.toString().isEmpty()) {
                    loginLayout.error = getString(R.string.emptyloginfield)
                    errorFlag = true
                }

                if (passField.text.toString().isEmpty()) {
                    passLayout.error = getString(R.string.emptypassfield)
                    errorFlag = true
                }
            }
            if (!errorFlag) {
                AndroidUtils.hideKeyboard(requireView())
                viewModel.updateUser(
                    binding.loginField.text.toString(),
                    binding.passField.text.toString()
                )

                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.authentication.collectLatest {
                            if (it) findNavController().navigateUp()
                        }
                    }
                }

                if (viewModel.errorMessage.value.isNotEmpty()) {
                    Toast.makeText(
                        context,
                        R.string.invalid_username_or_password,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        return binding.root
    }
}