package ru.netology.nework.adapter.job.myjob

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nework.databinding.CardJobBinding
import ru.netology.nework.model.Job

class MyJobAdapter (
    private val myJobInteractionListener: MyJobInteractionListener
) : ListAdapter<Job, MyJobViewHolder>(JobDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyJobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyJobViewHolder(binding,myJobInteractionListener)
    }

    override fun onBindViewHolder(holder: MyJobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }
}

class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}