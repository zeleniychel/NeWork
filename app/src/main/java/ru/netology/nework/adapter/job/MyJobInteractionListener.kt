package ru.netology.nework.adapter.job

import ru.netology.nework.model.Job
interface MyJobInteractionListener {
    fun onRemove(job: Job) {}
}