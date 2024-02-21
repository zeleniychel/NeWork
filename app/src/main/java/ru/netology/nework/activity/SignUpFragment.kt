package ru.netology.nework.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentSignUpBinding
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
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.registration)
        toolbar.menu.clear()

        val binding = FragmentSignUpBinding.inflate(layoutInflater)

        val photoResultContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val uri = it.data?.data ?: return@registerForActivityResult
                    val file = uri.toFile()
                    val fileExtension = file.extension.lowercase()
                    if (fileExtension == "jpeg" || fileExtension == "png") {
                        viewModel.setPhoto(uri, file)
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.avatar_format_error), Toast.LENGTH_SHORT
                        ).show()
                    }
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

        viewModel.photo.observe(viewLifecycleOwner) {
            binding.avatar.apply {
                setImageURI(viewModel.photo.value?.uri)
            }
        }

        binding.signUpButton.setOnClickListener {
            var errorFlag = false
            with(binding) {
                if (loginField.text.toString().isEmpty()) {
                    loginLayout.error = getString(R.string.emptyloginfield)
                    errorFlag = true
                }
                if (nameField.text.toString().isEmpty()) {
                    nameLayout.error = getString(R.string.emptynamefield)
                    errorFlag = true
                }
                if (passField.text.toString().isEmpty()) {
                    passLayout.error = getString(R.string.emptypassfield)
                    errorFlag = true
                }
                if (repeatPassField.text.toString().isEmpty()) {
                    repeatPassLayout.error = getString(R.string.emptypassfield)
                    errorFlag = true
                }
                if (repeatPassField.text.toString() != passField.text.toString()) {
                    repeatPassLayout.error = getString(R.string.passdontmatch)
                    errorFlag = true
                }
            }
            if (!errorFlag) {
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
                    Toast.makeText(
                        context,
                        R.string.invalid_login,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        return binding.root
    }
}