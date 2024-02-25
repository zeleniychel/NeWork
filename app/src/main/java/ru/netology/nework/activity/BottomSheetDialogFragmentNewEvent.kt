package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.BottomSheetDialogNewEventBinding
import ru.netology.nework.model.EventType
import ru.netology.nework.viewmodel.EventViewModel

@AndroidEntryPoint
class BottomSheetDialogFragmentNewEvent : BottomSheetDialogFragment() {
    private val viewModel by viewModels<EventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = BottomSheetDialogNewEventBinding.inflate(layoutInflater)

        dialog?.setOnDismissListener {
            viewModel.changeEventDate(binding.dateField.toString())
        }
        viewModel.event.observe(viewLifecycleOwner){
            if (viewModel.event.value?.type == EventType.ONLINE) {
                binding.onlineButton.isChecked = true
            } else {
                binding.offlineButton.isChecked = true
            }
            binding.dateField.editText?.setText(viewModel.event.value?.datetime)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.onlineButton -> {
                    viewModel.changeEventType(EventType.ONLINE)
                }
                R.id.offlineButton -> {
                    viewModel.changeEventType(EventType.OFFLINE)
                }
            }
        }
        return binding.root
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}