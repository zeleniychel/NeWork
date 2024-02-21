package ru.netology.nework.adapter.job.myjob

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nework.databinding.CardJobBinding
import ru.netology.nework.model.Job
import ru.netology.nework.util.formattedDateJob

class MyJobViewHolder(
    private val binding: CardJobBinding,
    private val myJobInteractionListener: MyJobInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(job: Job) {
        binding.apply {
            position.text = job.position
            company.text = job.name
            start.formattedDateJob(job.start)
            job.finish?.let { finish.formattedDateJob(it)}
            job.link?.let { link.text = it }
            removeJob.visibility = View.VISIBLE
            removeJob.setOnClickListener{
                myJobInteractionListener.onRemove(job)
            }

        }
    }
}