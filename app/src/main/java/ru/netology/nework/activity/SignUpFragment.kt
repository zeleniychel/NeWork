package ru.netology.nework.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentSignUpBinding
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.viewmodel.SignUpViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel by viewModels<SignUpViewModel>()
    private val photoResultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data ?: return@registerForActivityResult
                val file = uri.toFile()
                viewModel.setPhoto(uri, file)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).supportActionBar?.title = getString(R.string.registration)


        val binding = FragmentSignUpBinding.inflate(layoutInflater)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )

        binding.avatar.setOnClickListener {
            ImagePicker.Builder(this)
                .crop()
                .galleryOnly()
                .maxResultSize(2048, 2048)
                .createIntent(photoResultContract::launch)

            binding.avatar.setImageURI(viewModel.photo.value?.uri)
        }


        binding.signUpButton.setOnClickListener {
            if (binding.loginField.isEmpty()) {
                binding.loginField.error = getString(R.string.emptyloginfield)
                return@setOnClickListener
            }

            if (binding.passField.isEmpty()) {
                binding.passField.error = getString(R.string.emptypassfield)
                return@setOnClickListener
            }

            if (binding.repeatPassField.isEmpty()) {
                binding.repeatPassField.error = getString(R.string.emptypassfield)
                return@setOnClickListener
            }

            if (binding.nameField.isEmpty()) {
                binding.nameField.error = getString(R.string.emptynamefield)
                return@setOnClickListener
            }

            if (binding.passField.toString() != binding.repeatPassField.toString()) {
                binding.repeatPassField.error = getString(R.string.passdontmatch)
                return@setOnClickListener
            }

            AndroidUtils.hideKeyboard(requireView())

            val photoModel = viewModel.photo.value
            if (photoModel != null) {
                viewModel.registerUserWithAvatar(
                    binding.loginField.toString(),
                    binding.repeatPassField.toString(),
                    binding.nameField.toString(),
                    photoModel
                )
            } else {
                viewModel.registerUser(
                    binding.loginField.toString(),
                    binding.repeatPassField.toString(),
                    binding.nameField.toString())
            }
        }

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
        return binding.root
    }
}