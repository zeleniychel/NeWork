package ru.netology.nework.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSignUpBinding.inflate(layoutInflater)

        val photoResultContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val uri = it.data?.data ?: return@registerForActivityResult
                    val file = uri.toFile()
                    viewModel.setPhoto(uri, file)
                }
            }

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
        }

        viewModel.photo.observe(viewLifecycleOwner){
            binding.avatar.apply {
                setImageURI(viewModel.photo.value?.uri)
            }
        }


        binding.signUpButton.setOnClickListener {

            if (binding.loginField.text == null) {
                binding.loginField.error = getString(R.string.emptyloginfield)
                return@setOnClickListener
            }
            if (binding.nameField.text == null) {
                binding.nameField.error = getString(R.string.emptypassfield)
                return@setOnClickListener
            }

            if (binding.passField.text == null) {
                binding.passField.error = getString(R.string.emptypassfield)
                return@setOnClickListener
            }

            AndroidUtils.hideKeyboard(requireView())


            viewModel.registerUser(
                binding.loginField.text.toString(),
                binding.repeatPassField.text.toString(),
                binding.nameField.text.toString(),
                viewModel.photo.value
            )

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
        return binding.root
    }
}