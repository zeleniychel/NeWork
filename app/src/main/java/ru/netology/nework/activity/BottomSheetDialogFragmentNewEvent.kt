package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.databinding.BottomSheetDialogNewEventBinding

@AndroidEntryPoint
class BottomSheetDialogFragmentNewEvent : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = BottomSheetDialogNewEventBinding.inflate(layoutInflater)

        dialog?.setOnCancelListener {
            val result = Bundle().apply {
                putString("date", binding.dateInput.text.toString())
                putBoolean("type", binding.onlineButton.isChecked)
            }
            parentFragmentManager.setFragmentResult("resultKey", result)
        }

        return binding.root
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}