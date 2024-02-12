package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSignInBinding.inflate(layoutInflater)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )

        binding.signInButton.setOnClickListener {
            if (binding.loginField.text.toString().isEmpty()) {
                binding.loginField.error = getString(R.string.emptyloginfield)
                return@setOnClickListener
            }

            if (binding.passField.text.toString().isEmpty()) {
                binding.passField.error = getString(R.string.emptypassfield)
                return@setOnClickListener
            }
            AndroidUtils.hideKeyboard(requireView())
            viewModel.updateUser(
                binding.loginField.text.toString(),
                binding.passField.text.toString())

            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.authentication.collectLatest {
                        if (it) findNavController().navigateUp()
                    }
                }
            }
            if (viewModel.errorMessage.value.isNotEmpty()) {
                Snackbar.make(binding.root, viewModel.errorMessage.value, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        return binding.root
    }
}