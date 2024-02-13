package ru.netology.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.model.Event
import ru.netology.nework.repository.event.EventRepository
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
) : ViewModel() {
    private val _data = MutableLiveData<List<Event>>(emptyList())
    val data: LiveData<List<Event>> = _data

    init {
        getEvents()
    }

    private fun getEvents() = viewModelScope.launch {
        _data.value = repository.getEvents()
    }

    fun likeEventById(event: Event) = viewModelScope.launch {
        repository.likeEventById(event)
    }
}