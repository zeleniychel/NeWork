package ru.netology.nework.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentNewJobBinding
import ru.netology.nework.model.Job
import ru.netology.nework.viewmodel.MyWallViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class NewJobFragment: Fragment() {

    private val viewModel by viewModels<MyWallViewModel>()
    @Inject
    lateinit var appAuth: AppAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewJobBinding.inflate(layoutInflater)
        var startDate: String? = null
        var endDate: String? = null

        binding.createJobButton.setOnClickListener {
            viewModel.saveMyJob(
            Job(
                name = binding.nameField.text.toString(),
                position = binding.positionField.text.toString(),
                start = startDate.toString(),
                finish = endDate.toString(),
                link = binding.linkField.text.toString().takeIf { it.isNotEmpty() }
            ))
            findNavController().navigateUp()
        }
        binding.selectDateButton.setOnClickListener {
            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select dates")
                    .setTheme(R.style.CustomDateDialog)
                    .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                    .build()
            dateRangePicker.show(childFragmentManager, dateRangePicker.toString())
            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                startDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(selection.first)
                endDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(selection.second)
                val start = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selection.first)
                val end = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selection.second)
                binding.selectDateButton.text = "$start - $end"

            }

        }

        return binding.root
    }
}