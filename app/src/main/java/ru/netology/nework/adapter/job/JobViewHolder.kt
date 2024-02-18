package ru.netology.nework.adapter.job

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.databinding.CardJobBinding
import ru.netology.nework.model.Job
import ru.netology.nework.util.formattedDateJob

class JobViewHolder(
    private val binding: CardJobBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(job: Job) {
        binding.apply {
            position.text = job.position
            company.text = job.name
            start.formattedDateJob(job.start)
            job.finish?.let {
                finish.formattedDateJob(it)

            }
            job.link?.let {
                link.text = it
                link.visibility = View.VISIBLE
            }

        }
    }
}