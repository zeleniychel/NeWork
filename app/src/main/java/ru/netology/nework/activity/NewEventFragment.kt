package ru.netology.nework.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.netology.nework.model.Event
import ru.netology.nework.model.EventType
import ru.netology.nework.util.Converter
import ru.netology.nework.util.getParcelableCompat
import ru.netology.nework.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NewEventFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth
    private val viewModel by viewModels<EventViewModel>()
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
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.new_event)
        toolbar.menu.clear()

        val binding = FragmentNewEventBinding.inflate(layoutInflater)
        val eventArg = arguments?.getParcelableCompat<Event>("key")
        val resultListener = FragmentResultListener { _, result ->
            val list = result.getLongArray("list")?.toList()
            list?.let { viewModel.setSpeakerIds(it) }
        }


        val dateResultListener = FragmentResultListener { _, result ->
            val date = result.getString("date") ?: ""
            val type = result.getBoolean("type")
            val formatDate = Converter.convertDateFormat(date)
            viewModel.setEventDate(formatDate)
            viewModel.setEventType(if (type) EventType.ONLINE else EventType.OFFLINE)

        }



        parentFragmentManager.setFragmentResultListener("key", viewLifecycleOwner, resultListener)
        childFragmentManager.setFragmentResultListener("resultKey", viewLifecycleOwner, dateResultListener)


        viewModel.photo.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.remove.visibility = View.GONE
                return@observe
            }
            binding.remove.visibility = View.VISIBLE
            binding.imagePreview.setImageURI(it.uri)
            binding.imagePreview.visibility = View.VISIBLE
        }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.Builder(this)
                .crop()
                .galleryOnly()
                .maxResultSize(2048, 2048)
                .createIntent(photoResultContract::launch)
        }

        binding.editText.setText(eventArg?.content)
        viewModel.edit(eventArg ?: Event())

        binding.remove.setOnClickListener {
            viewModel.setPhoto(null, null)
            binding.remove.visibility = View.GONE
            binding.imagePreview.visibility = View.GONE
        }

        binding.pickAttachment.setOnClickListener {

        }

        binding.fabEventDetails.setOnClickListener {
            val bottomSheet = BottomSheetDialogFragmentNewEvent()
            bottomSheet.show(childFragmentManager, BottomSheetDialogFragmentNewEvent.TAG)
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
                    viewModel.save(binding.editText.text.toString())
                    findNavController().navigateUp()
                    true
                }

                else -> false
            }
        }

        return binding.root
    }
}