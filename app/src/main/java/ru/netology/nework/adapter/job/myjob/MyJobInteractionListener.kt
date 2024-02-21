package ru.netology.nework.adapter.job.myjob

import ru.netology.nework.model.Job
interface MyJobInteractionListener {
    fun onRemove(job: Job) {}
}