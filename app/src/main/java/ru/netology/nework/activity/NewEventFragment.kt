package ru.netology.nework.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentNewEventBinding
import ru.netology.nework.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NewEventFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth
    private val viewModel by viewModels<EventViewModel>()
    private var usersList:List<Long>? = null
    private val photoResultContract =
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.new_event)
        toolbar.menu.clear()

        val binding = FragmentNewEventBinding.inflate(layoutInflater)

        val resultListener = FragmentResultListener { _ ,result ->
            usersList = result.getLongArray("list")?.toList()
        }
        parentFragmentManager.setFragmentResultListener("key", viewLifecycleOwner, resultListener)

        viewModel.photo.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.remove.visibility = View.GONE
                return@observe
            }
            binding.remove.visibility = View.VISIBLE
            binding.imagePreview.setImageURI(it.uri)
        }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.Builder(this)
                .crop()
                .galleryOnly()
                .maxResultSize(2048, 2048)
                .createIntent(photoResultContract::launch)
        }

        binding.remove.setOnClickListener {
            viewModel.setPhoto(null, null)
            binding.remove.visibility = View.GONE
        }

        binding.pickAttachment.setOnClickListener {

        }

        binding.fabEventDetails.setOnClickListener {
            val bottomSheet = BottomSheetDialogFragmentNewEvent()
            bottomSheet.show(parentFragmentManager, BottomSheetDialogFragmentNewEvent.TAG)
        }

        binding.choseSpeakers.setOnClickListener {
            findNavController().navigate(R.id.action_newEventFragment_to_checkUsersFragment)
        }

        binding.makeMark.setOnClickListener {
            findNavController().navigate(R.id.action_newEventFragment_to_mapFragment)
        }

        toolbar.inflateMenu(R.menu.save_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.save -> {
                    findNavController().navigateUp()
                    true
                }

                else -> false
            }
        }

        return binding.root
    }
}